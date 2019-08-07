package tran.compbuildbackend.controllers.computerbuild;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import tran.compbuildbackend.controllers.utility.WebUtility;
import tran.compbuildbackend.domain.computerbuild.Purpose;

import java.net.URI;
import java.util.LinkedHashMap;

import static org.junit.Assert.*;
import static tran.compbuildbackend.constants.computerbuild.ComputerBuildConstants.*;
import static tran.compbuildbackend.constants.fields.ErrorKeyConstants.MESSAGE_KEY;
import static tran.compbuildbackend.constants.fields.FieldConstants.ID_FIELD;
import static tran.compbuildbackend.constants.mapping.MappingConstants.PURPOSE_API;
import static tran.compbuildbackend.constants.mapping.MappingConstants.URL_SEPARATOR;
import static tran.compbuildbackend.constants.tests.TestUtility.*;
import static tran.compbuildbackend.constants.users.UserConstants.*;
import static tran.compbuildbackend.controllers.utility.WebUtility.loginHelper;
import static tran.compbuildbackend.domain.utility.ComputerBuildDetailUtility.*;
import static tran.compbuildbackend.domain.utility.PurposeUtility.getPurposeAsJson;

@Profile("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class PurposeControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private String token;

    private String computerBuildIdentifier;

    @Before
    public void setUp() throws Exception {
        token = loginHelper(USER_NAME_TO_TEST_OWNERSHIP_ENDPOINTS, USER_PASSWORD, restTemplate);
        computerBuildIdentifier = setComputerBuildIdentifier(restTemplate);
    }

    /*
     * testing a purpose creation passing in required fields and logged in as the owner.
     */
    @Test
    public void createPurposeSuccess() throws Exception {
        generatePurpose();
    }

    /*
     * testing a purpose creation when not the owner.
     */
    @Test
    public void createPurposeNotAsOwner() throws Exception {
        LinkedHashMap response = generatePurposeFailure(DEFAULT_PRIORITY, TEST_PURPOSE_LIST_NOTE_ONE, true);
        assertNotNull(response.get(MESSAGE_KEY));
        assertEquals(COMPUTER_BUILD_CANNOT_BE_MODIFIED, response.get(MESSAGE_KEY));
    }

    /*
     * testing a purpose creation without including a description (required field).
     */
    @Test
    public void createPurposeFailure() throws Exception {
        LinkedHashMap response = generatePurposeFailure(DEFAULT_PRIORITY, EMPTY_CONTENT, false);
        assertNotNull(response.get(DESCRIPTION_KEY));
        assertEquals(FIELD_CANNOT_BE_BLANK, response.get(DESCRIPTION_KEY));
    }

    /*
     * testing a purpose update passing in required fields and logged in as the owner.
     */
    @Test
    public void updatePurposeSuccess() throws Exception {
        Purpose newPurpose = (Purpose) updatePurpose(false, true, TEST_PURPOSE_LIST_NOTE_TWO);
        assertNotNull(newPurpose);
        assertNotEquals(TEST_PURPOSE_LIST_NOTE_ONE, newPurpose.getDescription());
        assertEquals(TEST_PURPOSE_LIST_NOTE_TWO, newPurpose.getDescription());
    }

    /*
     * testing a purpose update passing in required fields but not as the owner.
     */
    @Test
    public void updatePurposeNotAsOwner() throws Exception {
        LinkedHashMap response = (LinkedHashMap) updatePurpose(true, false,
                TEST_PURPOSE_LIST_NOTE_ONE);
        assertNotNull(response);
        assertNotNull(response.get(MESSAGE_KEY));
        assertEquals(COMPUTER_BUILD_CANNOT_BE_MODIFIED, response.get(MESSAGE_KEY));
    }

    /*
     * testing a purpose update without passing in a description.
     */
    @Test
    public void updatePurposeFailure() throws Exception {
        LinkedHashMap response = (LinkedHashMap) updatePurpose(false, false, EMPTY_CONTENT);
        assertNotNull(response);
        assertNotNull(response.get(DESCRIPTION_KEY));
        assertEquals(FIELD_CANNOT_BE_BLANK, response.get(DESCRIPTION_KEY));
    }

    /*
     * testing a successful purpose deletion.
     */
    @Test
    public void deletePurposeSuccess() throws Exception {
        LinkedHashMap contents = createPurposeRequest(false, HttpMethod.DELETE, HttpStatus.OK.value(), true, true);
        assertNotNull(contents.get(MESSAGE_KEY));
        assertEquals(contents.get(MESSAGE_KEY), PURPOSE_DELETE_MESSAGE);
    }

    /*
     * testing a purpose deletion where the user logged in is not the owner so the deletion cannot be done.
     */
    @Test
    public void deletePurposeNotAsOwner() throws Exception {
        LinkedHashMap contents = createPurposeRequest(true, HttpMethod.DELETE, HttpStatus.BAD_REQUEST.value(),
                true,true);
        assertNotNull(contents.get(MESSAGE_KEY));
        assertEquals(contents.get(MESSAGE_KEY), COMPUTER_BUILD_CANNOT_BE_MODIFIED);
    }

    /*
     * testing a purpose deletion where the user is logged in but has entered in the incorrect identifier so the purpose
     * is not deleted.
     */
    @Test
    public void deletePurposeFailure() throws Exception {
        LinkedHashMap contents = createPurposeRequest(false, HttpMethod.DELETE, HttpStatus.BAD_REQUEST.value(),
                true,false);
        assertNotNull(contents.get(MESSAGE_KEY));
        assertEquals(contents.get(MESSAGE_KEY), PURPOSE_CANNOT_BE_DELETED);
    }

    /*
     * testing when the user is logged in and attempts to retrieve a purpose by the correct identifier.
     */
    @Test
    public void getPurposeLoggedInSuccess() throws Exception {
        LinkedHashMap contents = createPurposeRequest(false, HttpMethod.GET, HttpStatus.OK.value(),
                true,true);
        assertEquals(TEST_PURPOSE_LIST_NOTE_ONE, contents.get(DESCRIPTION_KEY));
        assertEquals(DEFAULT_PRIORITY, contents.get(PRIORITY_KEY));
        assertNotNull(contents.get(UNIQUE_IDENTIFIER_KEY));
        assertNotNull(contents.get(ID_FIELD));
    }

    /*
     * testing when the user is not logged in and attempts to retrieve a purpose by the correct identifier.
     */
    @Test
    public void getPurposeNotLoggedInSuccess() throws Exception {
        LinkedHashMap contents = createPurposeRequest(false, HttpMethod.GET, HttpStatus.OK.value(),
                false,true);
        assertEquals(TEST_PURPOSE_LIST_NOTE_ONE, contents.get(DESCRIPTION_KEY));
        assertEquals(DEFAULT_PRIORITY, contents.get(PRIORITY_KEY));
        assertNotNull(contents.get(UNIQUE_IDENTIFIER_KEY));
        assertNotNull(contents.get(ID_FIELD));
    }

    /*
     * testing when the user is logged in but provides the incorrect identifier when attempting to retrieve a purpose.
     */
    @Test
    public void getPurposeLoggedInFailure() throws Exception {
        LinkedHashMap contents = createPurposeRequest(false, HttpMethod.GET, HttpStatus.BAD_REQUEST.value(),
                true,false);
        assertNotNull(contents);
        assertEquals(INVALID_PURPOSE, contents.get(MESSAGE_KEY));
    }

    /*
     * testing when the user is not logged in and provides the incorrect identifier when attempting to retrieve a purpose.
     */
    @Test
    public void getPurposeNotLoggedInFailure() throws Exception {
        LinkedHashMap contents = createPurposeRequest(false, HttpMethod.GET, HttpStatus.BAD_REQUEST.value(),
                false,false);
        assertNotNull(contents);
        assertEquals(INVALID_PURPOSE, contents.get(MESSAGE_KEY));
    }

    /*
     * helper method to create a patch request that is successful and returns a purpose.
     */
    private Purpose updateRequestSuccess(String url, HttpEntity<String> entity) throws Exception {
        URI uri = new URI(url);
        return restTemplate.patchForObject(uri, entity, Purpose.class);
    }

    /*
     * helper method to persist/create a purpose object for testing.
     */
    private Purpose generatePurpose() throws Exception {
        String content = getPurposeAsJson(DEFAULT_PRIORITY, TEST_PURPOSE_LIST_NOTE_ONE);
        String createPurposeURL = BASE_URL + PURPOSE_API + computerBuildIdentifier;
        return (Purpose) createNote(content, createPurposeURL, HttpStatus.CREATED.value(), token, restTemplate,
                PURPOSE_TYPE, TEST_PURPOSE_LIST_NOTE_ONE, DEFAULT_PRIORITY);
    }

    /*
     * helper method to create a purpose when either not the owner or passing in invalid data.
     */
    private LinkedHashMap generatePurposeFailure(int priority, String description, boolean logInSecondTime)
            throws Exception {
        if (logInSecondTime) {
            // login as someone else.
            token = loginHelper(ANOTHER_USER_NAME_TO_CREATE_NEW_USER, USER_PASSWORD, restTemplate);
        }

        String content = getPurposeAsJson(priority, description);
        String createPurposeURL = BASE_URL + PURPOSE_API + computerBuildIdentifier;
        return createComputerDetailFailure(content, createPurposeURL, HttpStatus.BAD_REQUEST.value(), token,
                restTemplate);
    }

    /*
     * helper method to update a build note for successful and failing cases.
     */
    private Object updatePurpose(boolean logInSecondTime, boolean isSuccessfulUpdate, String description)
            throws Exception {
        Purpose Purpose = generatePurpose();

        if (logInSecondTime) {
            // login as someone else.
            token = loginHelper(ANOTHER_USER_NAME_TO_CREATE_NEW_USER, USER_PASSWORD, restTemplate);
        }

        String PurposeIdentifier = Purpose.getUniqueIdentifier();
        String content = getPurposeAsJson(DEFAULT_PRIORITY, description, computerBuildIdentifier, Purpose.getId(),
                PurposeIdentifier);

        String updatePurposeURL = BASE_URL + PURPOSE_API + computerBuildIdentifier + URL_SEPARATOR + PurposeIdentifier;
        return isSuccessfulUpdate
                ? updateRequestSuccess(updatePurposeURL, WebUtility.getEntityWithToken(content, token))
                : updateRequest(updatePurposeURL, WebUtility.getEntityWithToken(content, token), restTemplate);
    }

    /*
     * helper method to get or delete a purpose note.
     */
    private LinkedHashMap createPurposeRequest(boolean logInSecondTime, HttpMethod httpMethod, int expectedStatusCode,
            boolean loggedIn, boolean isSuccess) throws Exception {
        Purpose Purpose = generatePurpose();

        if (logInSecondTime) {
            token = loginHelper(ANOTHER_USER_NAME_TO_CREATE_NEW_USER, USER_PASSWORD, restTemplate);
        }
        String PurposeUniqueIdentifier = Purpose.getUniqueIdentifier();
        String url = isSuccess
                ? BASE_URL + PURPOSE_API + computerBuildIdentifier + URL_SEPARATOR + PurposeUniqueIdentifier
                : BASE_URL + PURPOSE_API + computerBuildIdentifier + URL_SEPARATOR + PurposeUniqueIdentifier
                        + INVALID_IDENTIFIER_SUFFIX;

        Object response = loggedIn ? createRequest(url, WebUtility.getEntityWithToken(null, token), expectedStatusCode, httpMethod,
                        restTemplate) : createRequest(url, WebUtility.getEntity(null), expectedStatusCode,
                httpMethod, restTemplate);
        assertNotNull(response);
        LinkedHashMap contents = (LinkedHashMap) response;
        assertNotNull(contents);
        return contents;
    }
}
