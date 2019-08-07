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
import tran.compbuildbackend.domain.computerbuild.BuildNote;

import java.net.URI;
import java.util.LinkedHashMap;

import static org.junit.Assert.*;
import static tran.compbuildbackend.constants.computerbuild.ComputerBuildConstants.*;
import static tran.compbuildbackend.constants.fields.ErrorKeyConstants.MESSAGE_KEY;
import static tran.compbuildbackend.constants.fields.FieldConstants.ID_FIELD;
import static tran.compbuildbackend.constants.mapping.MappingConstants.BUILD_NOTE_API;
import static tran.compbuildbackend.constants.mapping.MappingConstants.URL_SEPARATOR;
import static tran.compbuildbackend.constants.tests.TestUtility.*;
import static tran.compbuildbackend.constants.users.UserConstants.*;
import static tran.compbuildbackend.controllers.utility.WebUtility.loginHelper;
import static tran.compbuildbackend.domain.utility.BuildNoteUtility.getBuildNoteAsJson;
import static tran.compbuildbackend.domain.utility.ComputerBuildDetailUtility.*;

@Profile("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class BuildNoteControllerTest {

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
     * testing a build note creation passing in required fields and logged in as the owner.
     */
    @Test
    public void createBuildNoteSuccess() throws Exception {
        generateBuildNote();
    }

    /*
     * testing a build note creation when not the owner.
     */
    @Test
    public void createBuildNoteNotAsOwner() throws Exception {
        LinkedHashMap response = generateBuildNoteFailure(DEFAULT_PRIORITY, TEST_DIRECTION_DESCRIPTION, true);
        assertNotNull(response.get(MESSAGE_KEY));
        assertEquals(COMPUTER_BUILD_CANNOT_BE_MODIFIED, response.get(MESSAGE_KEY));
    }

    /*
     * testing a build note creation without including a description (required field).
     */
    @Test
    public void createBuildNoteFailure() throws Exception {
        LinkedHashMap response = generateBuildNoteFailure(DEFAULT_PRIORITY, EMPTY_CONTENT, false);
        assertNotNull(response.get(DESCRIPTION_KEY));
        assertEquals(FIELD_CANNOT_BE_BLANK, response.get(DESCRIPTION_KEY));
    }

    /*
     * testing a build note update passing in required fields and logged in as the owner.
     */
    @Test
    public void updateBuildNoteSuccess() throws Exception {
        BuildNote newBuildNote = (BuildNote) updateBuildNote(false, true, TEST_BUILD_NOTE_LIST_NOTE_TWO);
        assertNotNull(newBuildNote);
        assertNotEquals(TEST_BUILD_NOTE_LIST_NOTE_ONE, newBuildNote.getDescription());
        assertEquals(TEST_BUILD_NOTE_LIST_NOTE_TWO, newBuildNote.getDescription());
    }

    /*
     * testing a build note update passing in required fields but not as the owner.
     */
    @Test
    public void updateBuildNoteNotAsOwner() throws Exception {
        LinkedHashMap response = (LinkedHashMap) updateBuildNote(true, false, TEST_BUILD_NOTE_LIST_NOTE_TWO);
        assertNotNull(response);
        assertNotNull(response.get(MESSAGE_KEY));
        assertEquals(COMPUTER_BUILD_CANNOT_BE_MODIFIED, response.get(MESSAGE_KEY));
    }

    /*
     * testing a build note update without passing in a description.
     */
    @Test
    public void updateBuildNoteFailure() throws Exception {
        LinkedHashMap response = (LinkedHashMap) updateBuildNote(false, false, EMPTY_CONTENT);
        assertNotNull(response);
        assertNotNull(response.get(DESCRIPTION_KEY));
        assertEquals(FIELD_CANNOT_BE_BLANK, response.get(DESCRIPTION_KEY));
    }

    /*
     * testing a successful build note deletion.
     */
    @Test
    public void deleteBuildNoteSuccess() throws Exception {
        LinkedHashMap contents = createBuildNoteRequest(false, HttpMethod.DELETE, HttpStatus.OK.value(),
                true, true);
        assertNotNull(contents.get(MESSAGE_KEY));
        assertEquals(contents.get(MESSAGE_KEY), BUILD_NOTE_DELETE_MESSAGE);
    }

    /*
     * testing a build note deletion where the user logged in is not the owner so the deletion cannot be done.
     */
    @Test
    public void deleteBuildNoteNotAsOwner() throws Exception {
        LinkedHashMap contents = createBuildNoteRequest(true, HttpMethod.DELETE, HttpStatus.BAD_REQUEST.value(),
                true, true);
        assertNotNull(contents.get(MESSAGE_KEY));
        assertEquals(contents.get(MESSAGE_KEY), COMPUTER_BUILD_CANNOT_BE_MODIFIED);
    }

    /*
     * testing a build note deletion where the user is logged in but has entered in the incorrect identifier so the
     * build note is not deleted.
     */
    @Test
    public void deleteBuildNoteFailure() throws Exception {
        LinkedHashMap contents = createBuildNoteRequest(false, HttpMethod.DELETE, HttpStatus.BAD_REQUEST.value(),
                true, false);
        assertNotNull(contents.get(MESSAGE_KEY));
        assertEquals(contents.get(MESSAGE_KEY), BUILD_NOTE_CANNOT_BE_DELETED);
    }

    /*
     * testing when the user is logged in and attempts to retrieve a build note by the correct identifier.
     */
    @Test
    public void getBuildNoteLoggedInSuccess() throws Exception {
        LinkedHashMap contents = createBuildNoteRequest(false, HttpMethod.GET, HttpStatus.OK.value(),
                true, true);
        assertEquals(TEST_BUILD_NOTE_LIST_NOTE_ONE, contents.get(DESCRIPTION_KEY));
        assertEquals(DEFAULT_PRIORITY, contents.get(PRIORITY_KEY));
        assertNotNull(contents.get(UNIQUE_IDENTIFIER_KEY));
        assertNotNull(contents.get(ID_FIELD));
    }

    /*
     * testing when the user is not logged in and attempts to retrieve a build note by the correct identifier.
     */
    @Test
    public void getBuildNoteNotLoggedInSuccess() throws Exception {
        LinkedHashMap contents = createBuildNoteRequest(false, HttpMethod.GET, HttpStatus.OK.value(),
                false, true);
        assertEquals(TEST_BUILD_NOTE_LIST_NOTE_ONE, contents.get(DESCRIPTION_KEY));
        assertEquals(DEFAULT_PRIORITY, contents.get(PRIORITY_KEY));
        assertNotNull(contents.get(UNIQUE_IDENTIFIER_KEY));
        assertNotNull(contents.get(ID_FIELD));
    }

    /*
     * testing when the user is logged in but provides the incorrect identifier when attempting to retrieve
     * a build note.
     */
    @Test
    public void getBuildNoteLoggedInFailure() throws Exception {
        LinkedHashMap contents = createBuildNoteRequest(false, HttpMethod.GET, HttpStatus.BAD_REQUEST.value(),
                true, false);
        assertNotNull(contents);
        assertEquals(INVALID_BUILD_NOTE, contents.get(MESSAGE_KEY));
    }

    /*
     * testing when the user is not logged in and provides the incorrect identifier when attempting to retrieve
     * a build note.
     */
    @Test
    public void getBuildNoteNotLoggedInFailure() throws Exception {
        LinkedHashMap contents = createBuildNoteRequest(false, HttpMethod.GET, HttpStatus.BAD_REQUEST.value(),
                false, false);
        assertNotNull(contents);
        assertEquals(INVALID_BUILD_NOTE, contents.get(MESSAGE_KEY));
    }

    /*
     * helper method to create a patch request that is successful and returns a direction.
     */
    private BuildNote updateRequestSuccess(String url, HttpEntity<String> entity) throws Exception {
        URI uri = new URI(url);
        return restTemplate.patchForObject(uri, entity, BuildNote.class);
    }

    /*
     * helper method to persist/create a build note object for testing.
     */
    private BuildNote generateBuildNote() throws Exception {
        String content = getBuildNoteAsJson(DEFAULT_PRIORITY, TEST_BUILD_NOTE_LIST_NOTE_ONE);
        String createBuildNoteURL = BASE_URL + BUILD_NOTE_API + computerBuildIdentifier;
        return (BuildNote) createNote(content, createBuildNoteURL, HttpStatus.CREATED.value(), token, restTemplate,
                BUILD_NOTE_TYPE, TEST_BUILD_NOTE_LIST_NOTE_ONE, DEFAULT_PRIORITY);
    }

    /*
     * helper method to create a direction when either not the owner or passing in invalid data.
     */
    private LinkedHashMap generateBuildNoteFailure(int priority, String description, boolean logInSecondTime) throws Exception {
        if(logInSecondTime) {
            // login as someone else.
            token = loginHelper(ANOTHER_USER_NAME_TO_CREATE_NEW_USER, USER_PASSWORD, restTemplate);
        }

        String content = getBuildNoteAsJson(priority, description);
        String createBuildNoteURL = BASE_URL + BUILD_NOTE_API + computerBuildIdentifier;
        return createComputerDetailFailure(content, createBuildNoteURL, HttpStatus.BAD_REQUEST.value(),
                token, restTemplate);
    }

    /*
     * helper method to update a build note for successful and failing cases.
     */
    private Object updateBuildNote(boolean logInSecondTime, boolean isSuccessfulUpdate, String description) throws Exception {
        BuildNote buildNote = generateBuildNote();

        if(logInSecondTime) {
            // login as someone else.
            token = loginHelper(ANOTHER_USER_NAME_TO_CREATE_NEW_USER, USER_PASSWORD, restTemplate);
        }

        String buildNoteIdentifier = buildNote.getUniqueIdentifier();
        String content = getBuildNoteAsJson(DEFAULT_PRIORITY, description, computerBuildIdentifier,
                buildNote.getId(), buildNoteIdentifier);

        String updateBuildNoteURL = BASE_URL + BUILD_NOTE_API + computerBuildIdentifier + URL_SEPARATOR + buildNoteIdentifier;

        return isSuccessfulUpdate ? updateRequestSuccess(updateBuildNoteURL, WebUtility.getEntityWithToken(content, token)) :
            updateRequest(updateBuildNoteURL, WebUtility.getEntityWithToken(content, token), restTemplate);
    }

    /*
     * helper method to get or delete a build note.
     */
    private LinkedHashMap createBuildNoteRequest(boolean logInSecondTime, HttpMethod httpMethod, int expectedStatusCode,
                                                 boolean loggedIn, boolean isSuccess) throws Exception {
        BuildNote buildNote = generateBuildNote();

        if(logInSecondTime) {
            token = loginHelper(ANOTHER_USER_NAME_TO_CREATE_NEW_USER, USER_PASSWORD, restTemplate);
        }
        String buildNoteUniqueIdentifier = buildNote.getUniqueIdentifier();
        String url = isSuccess ? BASE_URL + BUILD_NOTE_API + computerBuildIdentifier + URL_SEPARATOR + buildNoteUniqueIdentifier :
                BASE_URL + BUILD_NOTE_API + computerBuildIdentifier + URL_SEPARATOR + buildNoteUniqueIdentifier + INVALID_IDENTIFIER_SUFFIX;

        Object response = loggedIn ? createRequest(url, WebUtility.getEntityWithToken(null, token), expectedStatusCode, httpMethod, restTemplate) :
                createRequest(url, WebUtility.getEntity(null), expectedStatusCode, httpMethod, restTemplate);

        assertNotNull(response);
        LinkedHashMap contents = (LinkedHashMap) response;
        assertNotNull(contents);
        return contents;
    }
}
