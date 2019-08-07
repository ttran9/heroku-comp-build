package tran.compbuildbackend.domain.utility;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import tran.compbuildbackend.controllers.utility.WebUtility;
import tran.compbuildbackend.domain.computerbuild.AbstractNote;
import tran.compbuildbackend.domain.computerbuild.BuildNote;
import tran.compbuildbackend.domain.computerbuild.OverclockingNote;
import tran.compbuildbackend.domain.computerbuild.Purpose;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static tran.compbuildbackend.constants.computerbuild.ComputerBuildConstants.*;
import static tran.compbuildbackend.constants.mapping.MappingConstants.COMPUTER_BUILD_API;
import static tran.compbuildbackend.constants.mapping.MappingConstants.USER_NAME_REQUEST;
import static tran.compbuildbackend.constants.tests.TestUtility.BASE_URL;
import static tran.compbuildbackend.constants.tests.TestUtility.BUILD_IDENTIFIER_KEY;
import static tran.compbuildbackend.constants.users.UserConstants.USER_NAME_TO_TEST_OWNERSHIP_ENDPOINTS;

public class ComputerBuildDetailUtility {
    /*
     * method to create a note and verifies some of its fields.
     */
    public static AbstractNote createNote(String content, String createDirectionBuildURL, int expectedStatusCode,
                                           String token, TestRestTemplate restTemplate, String noteType,
                                            String expectedDescription, int expectedPriority) throws Exception {
        Object response = createRequest(createDirectionBuildURL, WebUtility.getEntityWithToken(content, token), expectedStatusCode,
                HttpMethod.POST, restTemplate);
        LinkedHashMap contents = (LinkedHashMap) response;
        String json = new Gson().toJson(contents, LinkedHashMap.class);

        switch(noteType) {
            case OVERCLOCKING_NOTE_TYPE:
                OverclockingNote overclockingNote = new ObjectMapper().readValue(json, OverclockingNote.class);
                checkNoteFields(overclockingNote, expectedDescription, expectedPriority);
                return overclockingNote;
            case PURPOSE_TYPE:
                Purpose purpose = new ObjectMapper().readValue(json, Purpose.class);
                checkNoteFields(purpose, expectedDescription, expectedPriority);
                return purpose;
            case BUILD_NOTE_TYPE:
                BuildNote buildNote = new ObjectMapper().readValue(json, BuildNote.class);
                checkNoteFields(buildNote, expectedDescription, expectedPriority);
                return buildNote;
            default:
                break;
        }
        return null;
    }

    /*
     * helper method to check if the fields are valid in a note object.
     */
    private static void checkNoteFields(AbstractNote note, String expectedDescription, int expectedPriority) {
        assertEquals(expectedDescription, note.getDescription());
        assertEquals(expectedPriority, note.getPriority());
        assertNotNull(note.getId());
        assertNotNull(note.getUniqueIdentifier());
        assertNotNull(note.getCreatedAt());
        assertNotNull(note.getUpdatedAt());
    }

    /*
     * helper method to be able to create a variety of HTTP requests such as GET, POST, DELETE, and PATCH.
     */
    public static Object createRequest(String url, HttpEntity<String> entity, int expectedStatusCode, HttpMethod requestType,
                                 TestRestTemplate restTemplate) throws Exception {
        URI uri = new URI(url);
        ResponseEntity<Object> result = restTemplate.exchange(uri, requestType, entity, Object.class);
        assertNotNull(result);
        assertEquals(expectedStatusCode, result.getStatusCodeValue());
        return result.getBody();
    }

    /*
     * helper method to create a request to attempt to create a computer detail object (direction, build note,
     * overclocking note, or purpose).
     */
    public static LinkedHashMap createComputerDetailFailure(String content, String createComputerBuildURL, int expectedStatusCode,
                                                      String token, TestRestTemplate restTemplate) throws Exception {
        Object response = createRequest(createComputerBuildURL, WebUtility.getEntityWithToken(content, token),
                expectedStatusCode, HttpMethod.POST, restTemplate);
        return (LinkedHashMap) response;
    }

    /*
     * helper method to create a patch request that results in an error and returns an object (LinkedHashMap) with
     * information about the error.
     */
    public static Object updateRequest(String url, HttpEntity<String> entity, TestRestTemplate restTemplate) throws Exception {
        URI uri = new URI(url);
        return restTemplate.patchForObject(uri, entity, Object.class);
    }

    /*
     * method to set the computer build identifier to be able to attempt to modify (a) part(s) of a computer build.
     */
    public static String setComputerBuildIdentifier(TestRestTemplate restTemplate) throws Exception {
        String getAllComputerBuildsURL = BASE_URL + COMPUTER_BUILD_API;
        String getAllComputerBuildsFromUserURL = getAllComputerBuildsURL + USER_NAME_REQUEST + USER_NAME_TO_TEST_OWNERSHIP_ENDPOINTS;
        Object builds = createRequest(getAllComputerBuildsFromUserURL, WebUtility.getEntity(null), HttpStatus.OK.value(), HttpMethod.GET, restTemplate);
        List<LinkedHashMap> computerBuilds = (List<LinkedHashMap>) builds;
        LinkedHashMap contents = computerBuilds.get(computerBuilds.size()-1);

        return (String) contents.get(BUILD_IDENTIFIER_KEY);
    }


}
