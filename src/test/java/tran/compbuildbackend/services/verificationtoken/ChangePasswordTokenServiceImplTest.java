package tran.compbuildbackend.services.verificationtoken;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import tran.compbuildbackend.domain.security.ChangePasswordToken;
import tran.compbuildbackend.domain.user.ApplicationUser;
import tran.compbuildbackend.exceptions.request.GenericRequestException;
import tran.compbuildbackend.repositories.security.ChangePasswordTokenRepository;
import tran.compbuildbackend.repositories.users.ApplicationUserRepository;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static tran.compbuildbackend.constants.tests.TestUtility.INVALID_IDENTIFIER_SUFFIX;
import static tran.compbuildbackend.constants.users.UserConstants.*;

@Profile({"test"})
@RunWith(SpringRunner.class)
@SpringBootTest
public class ChangePasswordTokenServiceImplTest {
    @Autowired
    private ChangePasswordTokenRepository changePasswordTokenRepository;

    @Autowired
    private ApplicationUserRepository applicationUserRepository;

    private ChangePasswordTokenServiceImpl tokenService;

    private ApplicationUser secondUser;

    private String invalidToken;

    private String token;

    @Before
    public void setup() {
        tokenService = new ChangePasswordTokenServiceImpl(changePasswordTokenRepository);
        secondUser = applicationUserRepository.findByUsername(USER_NAME_TWO);
        token = tokenService.createVerificationToken(secondUser);

        invalidToken = token + INVALID_IDENTIFIER_SUFFIX;
    }

    @Transactional
    @After
    public void cleanUp() {
        changePasswordTokenRepository.deleteByToken(token);
    }

    /*
     * This test will check if the change password verification token can be created and because there is no issue a
     * token (as a string) will be returned and tested for.
     */
    @Transactional
    @Test
    public void createChangePasswordVerificationTokenSuccess() {
        ApplicationUser user = applicationUserRepository.findByUsername(USER_NAME_ONE);
        String createdToken = tokenService.createVerificationToken(user);
        assertNotNull(createdToken);
    }

    /*
     * This test will check if we can create a change password token for a user that doesn't exist and in this case
     * no token can be made and we expect the token returned to be null.
     */
    @Transactional
    @Test
    public void createChangePasswordVerificationTokenFail() {
        ApplicationUser user = applicationUserRepository.findByUsername(USER_NAME_FAIL_LOOK_UP);
        String createdToken = tokenService.createVerificationToken(user);
        assertNull(createdToken);
    }

    /*
     * This test will check if we can get the change password token for a user that exists. It is expected the token
     * can be retrieved because we have created a token before the tests for this class are run.
     */
    @Transactional
    @Test
    public void getChangePasswordVerificationTokenSuccess() {
        ChangePasswordToken changePasswordToken = tokenService.getVerificationToken(token);
        assertNotNull(changePasswordToken);
    }

    /*
     * This test will check if we can get the change password token for a user that exists. It is expected the token
     * cannot be retrieved because we are passing in a token that doesn't exist.
     */
    @Transactional
    @Test
    public void getChangePasswordVerificationTokenFailure() {
        ChangePasswordToken changePasswordToken = tokenService.getVerificationToken(invalidToken);
        assertNull(changePasswordToken);
    }

    /*
     * This test is checking if the change password token we are passing in is still valid, in other words it has not
     * yet expired. It is expected the token has not yet expired because it has not been 2700 seconds from when it was
     * created.
     */
    @Transactional
    @Test
    public void validateChangePasswordVerificationTokenSuccess() {
        ApplicationUser user = tokenService.validateVerificationToken(token);
        assertNotNull(user);
    }

    /*
     * This test is checking if the change password token we are passing in is still valid, in other words it has not
     * yet expired. It is expected that we get exception because we cannot retrieve the token.
     */
    @Transactional
    @Test(expected = GenericRequestException.class)
    public void validateChangePasswordVerificationTokenFailure() {
        tokenService.validateVerificationToken(invalidToken);
    }

}
