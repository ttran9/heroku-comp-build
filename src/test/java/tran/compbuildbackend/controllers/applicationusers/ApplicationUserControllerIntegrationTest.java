package tran.compbuildbackend.controllers.applicationusers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import tran.compbuildbackend.controllers.utility.WebUtility;
import tran.compbuildbackend.domain.utility.ApplicationUserUtility;
import tran.compbuildbackend.exceptions.request.GenericRequestExceptionResponse;
import tran.compbuildbackend.payload.email.JWTLoginSuccessResponse;
import tran.compbuildbackend.payload.email.RequestSuccessfulResponse;

import java.net.URI;
import java.util.LinkedHashMap;

import static org.junit.Assert.*;
import static tran.compbuildbackend.constants.fields.FieldConstants.*;
import static tran.compbuildbackend.constants.fields.FieldValueConstants.*;
import static tran.compbuildbackend.constants.mapping.MappingConstants.*;
import static tran.compbuildbackend.constants.messages.ResponseMessage.*;
import static tran.compbuildbackend.constants.tests.TestUtility.BASE_URL;
import static tran.compbuildbackend.constants.users.UserConstants.*;

@Profile({"test"})
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApplicationUserControllerIntegrationTest {
    @Autowired
    private TestRestTemplate restTemplate;

    /*
     * testing a successful registration (happy path)
     */
    @Test
    public void testSuccessfulRegistration() throws Exception {
        String content = ApplicationUserUtility.getUserAsJson(SUCCESSFUL_USER_NAME, SUCCESSFUL_USER_EMAIL, FULL_NAME, USER_PASSWORD, USER_PASSWORD);

        String registrationURL = BASE_URL + USERS_API + REGISTER_URL;

        RequestSuccessfulResponse contents = getContents(registrationURL, WebUtility.getEntity(content), HttpStatus.CREATED.value());
        assertNotNull(contents);
        String token = contents.getToken();

        assertNotNull(contents.getMessage());
        assertEquals(VERIFY_EMAIL_MESSAGE, contents.getMessage());
        assertNotNull(token);
        assertFalse(contents.isEnabled());

        // once we activate the account we need to check again if the account is enabled.
        String confirmEmail = BASE_URL + USERS_API + CONFIRM_REGISTRATION_URL + token;
        contents = getContents(confirmEmail, WebUtility.getEntity(content), HttpStatus.OK.value());
        assertNotNull(contents);
        assertEquals(SUCCESSFUL_ACCOUNT_ACTIVATION, contents.getMessage());
        assertTrue(contents.isEnabled());
    }

    /*
     * helper method to put the results of posting to an end point that will have information such as a message (such as
     * a success message), the status of if the user's account is activated (enabled), and if there is an associated
     * JWT token with the user.
     */
    private RequestSuccessfulResponse getContents(String url, HttpEntity<String> entity, int expectedHttpStatusCode) throws Exception{
        URI uri = new URI(url);
        ResponseEntity<RequestSuccessfulResponse> result = restTemplate.postForEntity(uri, entity, RequestSuccessfulResponse.class);
        assertNotNull(result);
        assertEquals(expectedHttpStatusCode, result.getStatusCode().value());
        return result.getBody();
    }

    /*
     * This test checks to see what happens when we are trying to registration a user that is already in the database.
     */
    @Test
    public void testUsernameAndEmailExistsRegistration() throws Exception {
        String content = ApplicationUserUtility.getUserAsJson(USER_NAME_ONE, USER_ONE_EMAIL, FULL_NAME, USER_PASSWORD, USER_PASSWORD);

        String registrationURL = BASE_URL + USERS_API + REGISTER_URL;
        LinkedHashMap contents = getLinkedHashMapContents(registrationURL, WebUtility.getEntity(content), HttpStatus.BAD_REQUEST.value());

        assertNotNull(contents);

        assertEquals(USER_NAME_ERROR + USER_NAME_ONE + ALREADY_EXISTS_ERROR, contents.get(USER_NAME_FIELD));
        assertEquals(EMAIL_ERROR + USER_ONE_EMAIL + ALREADY_EXISTS_ERROR, contents.get(EMAIL_FIELD));
    }

    /*
     * This test checks if the user enters in passwords that don't match when attempting to register and it is expected
     * there is an error indicating the passwords don't match.
     */
    @Test
    public void testPasswordMismatch() throws Exception {
        // testing to see what happens if the password fields don't match.
        String content = ApplicationUserUtility.getUserAsJson(SUCCESSFUL_USER_NAME_TWO, SUCCESSFUL_USER_EMAIL_TWO, FULL_NAME, USER_PASSWORD, USER_PASSWORD_TWO);

        String registrationURL = BASE_URL + USERS_API + REGISTER_URL;
        LinkedHashMap contents = getLinkedHashMapContents(registrationURL, WebUtility.getEntity(content), HttpStatus.BAD_REQUEST.value());

        assertNotNull(contents);
        assertNotNull(contents.get(CONFIRM_PASSWORD_FIELD));
        assertEquals(PASSWORD_MISMATCH_ERROR, contents.get(CONFIRM_PASSWORD_FIELD));
    }

    /*
     * testing for the following errors.
     * 1) the password is too short (not at least 6 characters).
     * 2) no full name field entered.
     * 3) The passwords don't match.
     */
    @Test
    public void testMultipleFieldErrors() throws Exception {

        String content = ApplicationUserUtility.getUserAsJson(USER_NAME_ONE, USER_ONE_EMAIL, SHORT_PASSWORD, USER_PASSWORD_TWO);

        String registrationURL = BASE_URL + USERS_API + REGISTER_URL;
        LinkedHashMap contents = getLinkedHashMapContents(registrationURL, WebUtility.getEntity(content), HttpStatus.BAD_REQUEST.value());

        assertNotNull(contents);
        assertNotNull(contents.get(CONFIRM_PASSWORD_FIELD));
        assertEquals(PASSWORD_MISMATCH_ERROR, contents.get(CONFIRM_PASSWORD_FIELD));
        assertNotNull(contents.get(PASSWORD_FIELD));
        assertEquals(SHORT_PASSWORD_ERROR, contents.get(PASSWORD_FIELD));
        assertNotNull(contents.get(FULL_NAME_FIELD));
        assertEquals(FULL_NAME_MISSING_ERROR, contents.get(FULL_NAME_FIELD));
    }

    /*
     * tests the happy path for logging in. this is done by logging in as a user that was already boot strapped into
     * the application's test database.
     */
    @Test
    public void testValidLogin() throws Exception {
        loginHelper(USER_NAME_ONE, USER_PASSWORD);
    }

    /*
     * tests when the incorrect credentials are entered for an existing user.
     */
    @Test
    public void testInvalidLogin() throws Exception {

        String content = ApplicationUserUtility.getLoginRequestAsJson(USER_NAME_ONE, USER_PASSWORD_TWO);
        String loginURL = BASE_URL + USERS_API + LOGIN_URL;

        LinkedHashMap contents = getLinkedHashMapContents(loginURL, WebUtility.getEntity(content), HttpStatus.BAD_REQUEST.value());

        assertNotNull(contents);
        assertEquals(INVALID_USERNAME_ERROR, contents.get(USER_NAME_FIELD));
        assertEquals(INVALID_PASSWORD_ERROR, contents.get(PASSWORD_FIELD));
    }

    /*
     * tests the case where a user name that does not exist is attemping to log in.
     * this will just return a generic "invalid username, invalid password." in the response.
     */
    @Test
    public void testInvalidLoginWithNonExistingUser() throws Exception {

        String content = ApplicationUserUtility.getLoginRequestAsJson(USER_NAME_DOES_NOT_EXIST, USER_PASSWORD);
        String loginURL = BASE_URL + USERS_API + LOGIN_URL;

        LinkedHashMap contents = getLinkedHashMapContents(loginURL, WebUtility.getEntity(content), HttpStatus.BAD_REQUEST.value());

        assertNotNull(contents);
        assertEquals(INVALID_USERNAME_ERROR, contents.get(USER_NAME_FIELD));
        assertEquals(INVALID_PASSWORD_ERROR, contents.get(PASSWORD_FIELD));
    }

    /*
     * helper method to put results of a POST request to a LinkedHashMap.
     */
    private LinkedHashMap getLinkedHashMapContents(String url, HttpEntity<String> entity, int expectedHttpStatusCode) throws Exception{
        URI uri = new URI(url);
        ResponseEntity<LinkedHashMap> result = restTemplate.postForEntity(uri, entity, LinkedHashMap.class);
        assertNotNull(result);
        assertEquals(expectedHttpStatusCode, result.getStatusCode().value());
        return result.getBody();
    }

    /*
     * tests the happy path for the user requesting to change the password on the account.
     * this test will also attempt to log in after the password is changed.
     */
    @Test
    public void testChangePassword() throws Exception {

        String content = ApplicationUserUtility.getInitialPasswordChangeRequestAsJson(USER_ONE_EMAIL);
        String changePasswordURL = BASE_URL + USERS_API + CHANGE_PASSWORD_URL;

        // request for the password change.
        RequestSuccessfulResponse contents = getContents(changePasswordURL, WebUtility.getEntity(content), HttpStatus.OK.value());

        assertNotNull(contents);
        assertEquals(CHANGE_PASSWORD, contents.getMessage());
        assertNotNull(contents.getToken());

        // change the password.
        String passwordChangeRequestContent = ApplicationUserUtility.getPasswordChangeRequestAsJson(MODIFIED_PASSWORD, MODIFIED_PASSWORD);
        String processChangePasswordURL = BASE_URL + USERS_API + CHANGE_PASSWORD_URL + contents.getToken();
        RequestSuccessfulResponse response = getContents(processChangePasswordURL, WebUtility.getEntity(passwordChangeRequestContent), HttpStatus.OK.value());

        assertNotNull(response);
        assertEquals(PASSWORD_CHANGED, response.getMessage());

        // attempt to log in with the new password.
        loginHelper(USER_NAME_ONE, MODIFIED_PASSWORD);
    }

    /*
     * helper method to post to the login endpoint.
     */
    private void loginHelper(String userName, String password) throws Exception {
        String loginContent = ApplicationUserUtility.getLoginRequestAsJson(userName, password);
        String loginURL = BASE_URL + USERS_API + LOGIN_URL;
        URI uri = new URI(loginURL);

        ResponseEntity<JWTLoginSuccessResponse> result = restTemplate.postForEntity(uri, WebUtility.getEntity(loginContent), JWTLoginSuccessResponse.class);
        JWTLoginSuccessResponse loginResponse = result.getBody();

        assertNotNull(result);
        assertEquals(HttpStatus.OK.value(), result.getStatusCode().value());
        assertNotNull(loginResponse);
        assertNotNull(loginResponse.getToken());
        assertTrue(loginResponse.getSuccess());
    }

    /*
     * tests for when we attempt to change the password when the user does not exist.
     */
    @Test
    public void testChangePasswordWithNonExistingUsername() throws Exception {
        String content = ApplicationUserUtility.getInitialPasswordChangeRequestAsJson(USER_EMAIL_DOES_NOT_EXIST);
        String changePasswordURL = BASE_URL + USERS_API + CHANGE_PASSWORD_URL;

        URI uri = new URI(changePasswordURL);
        ResponseEntity<GenericRequestExceptionResponse> result = restTemplate.postForEntity(uri,
                WebUtility.getEntity(content), GenericRequestExceptionResponse.class);

        GenericRequestExceptionResponse contents = result.getBody();

        assertNotNull(contents);
        assertEquals(PASSWORD_CANNOT_BE_CHANGED_FOR_INVALID_USER, contents.getUsername());
    }

}
