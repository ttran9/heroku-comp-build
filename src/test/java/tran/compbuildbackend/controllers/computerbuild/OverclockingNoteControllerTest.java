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
import tran.compbuildbackend.domain.computerbuild.OverclockingNote;

import java.net.URI;
import java.util.LinkedHashMap;

import static org.junit.Assert.*;
import static tran.compbuildbackend.constants.computerbuild.ComputerBuildConstants.*;
import static tran.compbuildbackend.constants.fields.ErrorKeyConstants.MESSAGE_KEY;
import static tran.compbuildbackend.constants.fields.FieldConstants.ID_FIELD;
import static tran.compbuildbackend.constants.mapping.MappingConstants.OVERCLOCKING_NOTE_API;
import static tran.compbuildbackend.constants.mapping.MappingConstants.URL_SEPARATOR;
import static tran.compbuildbackend.constants.tests.TestUtility.*;
import static tran.compbuildbackend.constants.users.UserConstants.*;
import static tran.compbuildbackend.controllers.utility.WebUtility.loginHelper;
import static tran.compbuildbackend.domain.utility.ComputerBuildDetailUtility.*;
import static tran.compbuildbackend.domain.utility.OverclockingNoteUtility.getOverclockingNoteAsJson;

@Profile("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class OverclockingNoteControllerTest {

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
     * testing a purpose creation passing in required fields and logged in as the
     * owner.
     */
    @Test
    public void createOverclockingNoteSuccess() throws Exception {
        generateOverclockingNote();
    }

    /*
     * testing a OverclockingNote creation when not the owner.
     */
    @Test
    public void createOverclockingNoteNotAsOwner() throws Exception {
        LinkedHashMap response = generateOverclockingNoteFailure(DEFAULT_PRIORITY, TEST_OVERCLOCKING_NOTE_LIST_NOTE_ONE,
                true);
        assertNotNull(response.get(MESSAGE_KEY));
        assertEquals(COMPUTER_BUILD_CANNOT_BE_MODIFIED, response.get(MESSAGE_KEY));
    }

    /*
     * testing a OverclockingNote creation without including a description (required
     * field).
     */
    @Test
    public void createOverclockingNoteFailure() throws Exception {
        LinkedHashMap response = generateOverclockingNoteFailure(DEFAULT_PRIORITY, EMPTY_CONTENT, false);
        assertNotNull(response.get(DESCRIPTION_KEY));
        assertEquals(FIELD_CANNOT_BE_BLANK, response.get(DESCRIPTION_KEY));
    }

    /*
     * testing a OverclockingNote update passing in required fields and logged in as the owner.
     */
    @Test
    public void updateOverclockingNoteSuccess() throws Exception {
        OverclockingNote newOverclockingNote = (OverclockingNote) updateOverclockingNote(false,true,
                TEST_OVERCLOCKING_NOTE_LIST_NOTE_TWO);
        assertNotNull(newOverclockingNote);
        assertNotEquals(TEST_OVERCLOCKING_NOTE_LIST_NOTE_ONE, newOverclockingNote.getDescription());
        assertEquals(TEST_OVERCLOCKING_NOTE_LIST_NOTE_TWO, newOverclockingNote.getDescription());
    }

    /*
     * testing an overclocking note update passing in required fields but not as the owner.
     */
    @Test
    public void updateOverclockingNoteNotAsOwner() throws Exception {
        LinkedHashMap response = (LinkedHashMap) updateOverclockingNote(true, false,
                TEST_OVERCLOCKING_NOTE_LIST_NOTE_ONE);
        assertNotNull(response);
        assertNotNull(response.get(MESSAGE_KEY));
        assertEquals(COMPUTER_BUILD_CANNOT_BE_MODIFIED, response.get(MESSAGE_KEY));
    }

    /*
     * testing an overclocking note update without passing in a description.
     */
    @Test
    public void updateOverclockingNoteFailure() throws Exception {
        LinkedHashMap response = (LinkedHashMap) updateOverclockingNote(false, false, EMPTY_CONTENT);
        assertNotNull(response);
        assertNotNull(response.get(DESCRIPTION_KEY));
        assertEquals(FIELD_CANNOT_BE_BLANK, response.get(DESCRIPTION_KEY));
    }

    /*
     * testing a successful OverclockingNote deletion.
     */
    @Test
    public void deleteOverclockingNoteSuccess() throws Exception {
        LinkedHashMap contents = createOverclockingNoteRequest(false, HttpMethod.DELETE, HttpStatus.OK.value(), true,
                true);
        assertNotNull(contents.get(MESSAGE_KEY));
        assertEquals(contents.get(MESSAGE_KEY), OVERCLOCKING_NOTE_DELETE_MESSAGE);
    }

    /*
     * testing an overclocking note deletion where the user logged in is not the owner so the deletion cannot be done.
     */
    @Test
    public void deleteOverclockingNoteNotAsOwner() throws Exception {
        LinkedHashMap contents = createOverclockingNoteRequest(true, HttpMethod.DELETE, HttpStatus.BAD_REQUEST.value(),
                true, true);
        assertNotNull(contents.get(MESSAGE_KEY));
        assertEquals(contents.get(MESSAGE_KEY), COMPUTER_BUILD_CANNOT_BE_MODIFIED);
    }

    /*
     * testing a OverclockingNote deletion where the user is logged in but has entered in the incorrect identifier
     * so the overclocking note is not deleted.
     */
    @Test
    public void deleteOverclockingNoteFailure() throws Exception {
        LinkedHashMap contents = createOverclockingNoteRequest(false, HttpMethod.DELETE, HttpStatus.BAD_REQUEST.value(),
                true, false);
        assertNotNull(contents.get(MESSAGE_KEY));
        assertEquals(contents.get(MESSAGE_KEY), OVERCLOCKING_NOTE_CANNOT_BE_DELETED);
    }

    /*
     * testing when the user is logged in and attempts to retrieve a overclocking note by the correct identifier.
     */
    @Test
    public void getOverclockingNoteLoggedInSuccess() throws Exception {
        LinkedHashMap contents = createOverclockingNoteRequest(false, HttpMethod.GET, HttpStatus.OK.value(), true,
                true);
        assertEquals(TEST_OVERCLOCKING_NOTE_LIST_NOTE_ONE, contents.get(DESCRIPTION_KEY));
        assertEquals(DEFAULT_PRIORITY, contents.get(PRIORITY_KEY));
        assertNotNull(contents.get(UNIQUE_IDENTIFIER_KEY));
        assertNotNull(contents.get(ID_FIELD));
    }

    /*
     * testing when the user is not logged in and attempts to retrieve a overclocking note by the correct identifier.
     */
    @Test
    public void getOverclockingNoteNotLoggedInSuccess() throws Exception {
        LinkedHashMap contents = createOverclockingNoteRequest(false, HttpMethod.GET, HttpStatus.OK.value(), false,
                true);
        assertEquals(TEST_OVERCLOCKING_NOTE_LIST_NOTE_ONE, contents.get(DESCRIPTION_KEY));
        assertEquals(DEFAULT_PRIORITY, contents.get(PRIORITY_KEY));
        assertNotNull(contents.get(UNIQUE_IDENTIFIER_KEY));
        assertNotNull(contents.get(ID_FIELD));
    }

    /*
     * testing when the user is logged in but provides the incorrect identifier when attempting to retrieve n overclocking note.
     */
    @Test
    public void getOverclockingNoteLoggedInFailure() throws Exception {
        LinkedHashMap contents = createOverclockingNoteRequest(false, HttpMethod.GET, HttpStatus.BAD_REQUEST.value(),
                true, false);
        assertNotNull(contents);
        assertEquals(INVALID_OVERCLOCKING_NOTE, contents.get(MESSAGE_KEY));
    }

    /*
     * testing when the user is not logged in and provides the incorrect identifier when attempting to retrieve a overclocking note.
     */
    @Test
    public void getOverclockingNoteNotLoggedInFailure() throws Exception {
        LinkedHashMap contents = createOverclockingNoteRequest(false, HttpMethod.GET, HttpStatus.BAD_REQUEST.value(),
                false, false);
        assertNotNull(contents);
        assertEquals(INVALID_OVERCLOCKING_NOTE, contents.get(MESSAGE_KEY));
    }

    /*
     * helper method to create a patch request that is successful and returns an overclocking note.
     */
    private OverclockingNote updateRequestSuccess(String url, HttpEntity<String> entity) throws Exception {
        URI uri = new URI(url);
        return restTemplate.patchForObject(uri, entity, OverclockingNote.class);
    }

    /*
     * helper method to persist/create a overclocking note object for testing.
     */
    private OverclockingNote generateOverclockingNote() throws Exception {
        String content = getOverclockingNoteAsJson(DEFAULT_PRIORITY, TEST_OVERCLOCKING_NOTE_LIST_NOTE_ONE);
        String createOverclockingNoteURL = BASE_URL + OVERCLOCKING_NOTE_API + computerBuildIdentifier;
        return (OverclockingNote) createNote(content, createOverclockingNoteURL, HttpStatus.CREATED.value(), token,
                restTemplate, OVERCLOCKING_NOTE_TYPE, TEST_OVERCLOCKING_NOTE_LIST_NOTE_ONE, DEFAULT_PRIORITY);
    }

    /*
     * helper method to create an overclocking note when either not the owner or passing in invalid data.
     */
    private LinkedHashMap generateOverclockingNoteFailure(int priority, String description, boolean logInSecondTime)
            throws Exception {
        if (logInSecondTime) {
            // login as someone else.
            token = loginHelper(ANOTHER_USER_NAME_TO_CREATE_NEW_USER, USER_PASSWORD, restTemplate);
        }

        String content = getOverclockingNoteAsJson(priority, description);
        String createOverclockingNoteURL = BASE_URL + OVERCLOCKING_NOTE_API + computerBuildIdentifier;
        return createComputerDetailFailure(content, createOverclockingNoteURL, HttpStatus.BAD_REQUEST.value(), token,
                restTemplate);
    }

    /*
     * helper method to update a build note for successful and failing cases.
     */
    private Object updateOverclockingNote(boolean logInSecondTime, boolean isSuccessfulUpdate, String description)
            throws Exception {
        OverclockingNote overclockingNote = generateOverclockingNote();

        if (logInSecondTime) {
            // login as someone else.
            token = loginHelper(ANOTHER_USER_NAME_TO_CREATE_NEW_USER, USER_PASSWORD, restTemplate);
        }

        String OverclockingNoteIdentifier = overclockingNote.getUniqueIdentifier();
        String content = getOverclockingNoteAsJson(DEFAULT_PRIORITY, description, computerBuildIdentifier,
                overclockingNote.getId(), OverclockingNoteIdentifier);

        String updateOverclockingNoteURL = BASE_URL + OVERCLOCKING_NOTE_API + computerBuildIdentifier + URL_SEPARATOR
                + OverclockingNoteIdentifier;
        return isSuccessfulUpdate ? updateRequestSuccess(updateOverclockingNoteURL, WebUtility.getEntityWithToken(content, token))
                : updateRequest(updateOverclockingNoteURL, WebUtility.getEntityWithToken(content, token), restTemplate);
    }

    /*
     * helper method to get or delete a build note.
     */
    private LinkedHashMap createOverclockingNoteRequest(boolean logInSecondTime, HttpMethod httpMethod,
            int expectedStatusCode, boolean loggedIn, boolean isSuccess) throws Exception {
        OverclockingNote OverclockingNote = generateOverclockingNote();

        if (logInSecondTime) {
            token = loginHelper(ANOTHER_USER_NAME_TO_CREATE_NEW_USER, USER_PASSWORD, restTemplate);
        }
        String OverclockingNoteUniqueIdentifier = OverclockingNote.getUniqueIdentifier();
        String url = isSuccess ? BASE_URL + OVERCLOCKING_NOTE_API + computerBuildIdentifier + URL_SEPARATOR + OverclockingNoteUniqueIdentifier :
                BASE_URL + OVERCLOCKING_NOTE_API + computerBuildIdentifier + URL_SEPARATOR  + OverclockingNoteUniqueIdentifier + INVALID_IDENTIFIER_SUFFIX;

        Object response = loggedIn ? createRequest(url, WebUtility.getEntityWithToken(null, token), expectedStatusCode, httpMethod,
                        restTemplate) : createRequest(url, WebUtility.getEntity(null), expectedStatusCode, httpMethod, restTemplate);
        assertNotNull(response);
        LinkedHashMap contents = (LinkedHashMap) response;
        assertNotNull(contents);
        return contents;
    }
}
