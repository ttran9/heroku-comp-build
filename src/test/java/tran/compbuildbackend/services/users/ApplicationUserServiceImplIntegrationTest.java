package tran.compbuildbackend.services.users;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Profile;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import tran.compbuildbackend.domain.user.ApplicationUser;
import tran.compbuildbackend.exceptions.request.GenericRequestException;
import tran.compbuildbackend.repositories.security.ChangePasswordTokenRepository;
import tran.compbuildbackend.repositories.security.EmailVerificationTokenRepository;
import tran.compbuildbackend.repositories.users.ApplicationUserRepository;

import static org.junit.Assert.*;
import static tran.compbuildbackend.constants.exception.ExceptionConstants.EXCEPTION_EMAIL_NAME_DOES_NOT_EXIST;
import static tran.compbuildbackend.constants.exception.ExceptionConstants.EXCEPTION_REQUEST_PASSWORD_CHANGE_FAILED;
import static tran.compbuildbackend.constants.users.UserConstants.*;

@Profile({"test"})
@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationUserServiceImplIntegrationTest {

    @Autowired
    private ApplicationUserRepository applicationUserRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private EmailVerificationTokenRepository emailVerificationTokenRepository;

    @Autowired
    private ChangePasswordTokenRepository changePasswordTokenRepository;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    private ApplicationUserService applicationUserService;

    @Before
    public void setup() {
        applicationUserService = new ApplicationUserServiceImpl(applicationUserRepository, bCryptPasswordEncoder,
                emailVerificationTokenRepository, changePasswordTokenRepository, eventPublisher);
    }

    /*
     * The below is testing the service to be able to create the user as well as send out an email while also creating
     * the EmailVerification token.
     */
    @Test
    public void testPersistUserWithNoEmailToken() {
        ApplicationUser user = createSampleTestUser(FULL_NAME_ONE, USER_NAME_DOES_NOT_EXIST_TWO, USER_EMAIL_DOES_NOT_EXIST_TWO, MODIFIED_PASSWORD);

        ApplicationUser userTwo = createSecondSampleTestUser(FULL_NAME_ONE, USER_NAME_ONE, USER_ONE_EMAIL, MODIFIED_PASSWORD);

        ApplicationUser returnedUser = applicationUserService.persistUser(user, null);

        assertEquals(userTwo.getConfirmPassword(), returnedUser.getConfirmPassword());
        assertFalse(returnedUser.isEnabled());
        assertNull(returnedUser.getEmailVerificationToken()); // there is no email sent so no token is generated.
    }

    /*
     * This test checks the service method that attempts to send out an email but this wil thrown a custom exception
     * because there is no request.
     */
    @Test(expected = GenericRequestException.class)
    public void testSendSuccessRegistrationEmailFail() {
        ApplicationUser user = createSampleTestUser(FULL_NAME_ONE, USER_NAME_DOES_NOT_EXIST_TWO, USER_EMAIL_DOES_NOT_EXIST_TWO, MODIFIED_PASSWORD);
        applicationUserService.sendSuccessRegistrationEmail(user, null);
    }

    /*
     * The test below is testing if the service to be able to create the user but not send out the email so there is
     * no token.
     */
    @Test
    public void testPersistUserWithSuccessfulEmail() {
        ApplicationUser user = createSampleTestUser(FULL_NAME_ONE, USER_NAME_TO_CREATE_NEW_USER, USER_EMAIL_TO_CREATE_NEW_USER, MODIFIED_PASSWORD);

        ApplicationUser userTwo = createSecondSampleTestUser(FULL_NAME_ONE, USER_NAME_ONE, USER_ONE_EMAIL, MODIFIED_PASSWORD);

        ApplicationUser returnedUser = applicationUserService.persistUser(user, new MockHttpServletRequest());

        assertEquals(userTwo.getConfirmPassword(), returnedUser.getConfirmPassword());
        assertFalse(returnedUser.isEnabled());
        assertNotNull(returnedUser.getEmailVerificationToken());

    }

    private ApplicationUser createSampleTestUser(String fullName, String userName, String email, String password) {
        ApplicationUser user = new ApplicationUser();
        user.setFullName(fullName);
        user.setUsername(userName);
        user.setEmail(email);
        user.setPassword(password);
        user.setConfirmPassword(password);
        user.setEnabled(true); // if the user can set the user to be activated it is expected below that the created account is not enabled (activated).
        return user;
    }

    private ApplicationUser createSecondSampleTestUser(String fullName, String userName, String email, String password) {
        ApplicationUser userTwo = new ApplicationUser();
        userTwo.setFullName(fullName);
        userTwo.setUsername(userName);
        userTwo.setEmail(email);
        userTwo.setPassword(password);
        userTwo.setConfirmPassword("");
        return userTwo;
    }

    /*
     * The test below is checking if a user that is not yet enabled can be enabled.
     */
    @Test
    public void testEnableUser() {
        ApplicationUser user = applicationUserService.getUserByUsername(USER_NAME_TWO);
        assertFalse(user.isEnabled());

        applicationUserService.enableUser(user);
        assertTrue(user.isEnabled());
    }

    /*
     * The test below checks if a custom exception is thrown if a user name being searched for does not exist.
     */
    @Test(expected = GenericRequestException.class)
    public void getUserByEmailDoesNotExist() {
        applicationUserService.getUserByEmail(USER_EMAIL_FAIL_LOOK_UP, EXCEPTION_EMAIL_NAME_DOES_NOT_EXIST);
    }

    /*
     * The test below checks if a custom exception is thrown if a user name is being searched for while trying to
     * request for a password change.
     */
//    @Test(expected = RequestChangePasswordException.class)
    @Test(expected = GenericRequestException.class)
    public void getUserByEmailCannotChangePassword() {
        applicationUserService.getUserByEmail(USER_EMAIL_FAIL_LOOK_UP, EXCEPTION_REQUEST_PASSWORD_CHANGE_FAILED);
    }

    /*
     * The test below checks if a custom exception is thrown if a change password is requested but the request consists
     * of a user name that doesn't exist.
     */
    @Test(expected = GenericRequestException.class)
    public void changePasswordWithoutToken() {
        ApplicationUser user = applicationUserService.getUserByEmail(USER_EMAIL_FAIL_LOOK_UP, EXCEPTION_EMAIL_NAME_DOES_NOT_EXIST);
        applicationUserService.changeUserPassword(MODIFIED_PASSWORD, user);
    }

    /*
     * The test below checks if a custom exception is thrown if a change password is requested but the request does
     * not exist.
     */
    @Test(expected = GenericRequestException.class)
    public void sendPasswordChangeEmailFailure() {
        applicationUserService.sendPasswordChangeEmail(USER_ONE_EMAIL, null);
    }
 }
