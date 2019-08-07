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
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import tran.compbuildbackend.controllers.utility.WebUtility;
import tran.compbuildbackend.domain.utility.ComputerBuildUtility;
import tran.compbuildbackend.dto.computerbuild.ComputerBuildDto;
import tran.compbuildbackend.exceptions.request.GenericRequestExceptionResponse;
import tran.compbuildbackend.payload.computerbuild.ComputerBuildResponse;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.List;

import static org.junit.Assert.*;
import static tran.compbuildbackend.constants.computerbuild.ComputerBuildConstants.COMPUTER_BUILD_CANNOT_BE_MODIFIED;
import static tran.compbuildbackend.constants.computerbuild.ComputerBuildConstants.COMPUTER_BUILD_DOES_NOT_EXIST;
import static tran.compbuildbackend.constants.fields.ErrorKeyConstants.MESSAGE_KEY;
import static tran.compbuildbackend.constants.mapping.MappingConstants.COMPUTER_BUILD_API;
import static tran.compbuildbackend.constants.mapping.MappingConstants.USER_NAME_REQUEST;
import static tran.compbuildbackend.constants.tests.TestUtility.*;
import static tran.compbuildbackend.constants.users.UserConstants.*;
import static tran.compbuildbackend.controllers.utility.WebUtility.loginHelper;

@Profile({"test"})
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ComputerBuildControllerIntegrationTest {
    @Autowired
    private TestRestTemplate restTemplate;

    private String token;

    @Before
    public void setUp() throws Exception {
        token = loginHelper(ANOTHER_USER_NAME_TO_CREATE_NEW_USER, USER_PASSWORD, restTemplate);
    }

    /*
     * testing a successful computer build creation (happy path)
     */
    @Test
    public void testCreateComputerBuildSuccess() throws Exception {
        String content = ComputerBuildUtility.getComputerBuildAsJson(SAMPLE_BUDGET_COMPUTER_BUILD_NAME, SAMPLE_GAMING_COMPUTER_BUILD_DESCRIPTION);

        String createComputerBuildURL = BASE_URL + COMPUTER_BUILD_API;

        createComputerBuild(content, createComputerBuildURL);
    }

    /*
     * testing a computer build registration creation that does not work because the computer build name is not passed in
     * but is required.
     */
    @Test
    public void testCreateComputerBuildFailure() throws Exception {
        String content = ComputerBuildUtility.getComputerBuildAsJson(null, null);

        String createComputerBuildURL = BASE_URL + COMPUTER_BUILD_API;

        ComputerBuildResponse contents = getContents(createComputerBuildURL, WebUtility.getEntityWithToken(content, token), HttpStatus.BAD_REQUEST.value());
        assertNotNull(contents);
        assertEquals(FIELD_CANNOT_BE_NULL, contents.getName());
    }

    /*
     * testing if a computer build that was created with a build identifier can be retrieved.
     */
    @Test
    public void testGetComputerBuildByIdentifier() throws Exception {
        String content = ComputerBuildUtility.getComputerBuildAsJson(SAMPLE_BUDGET_COMPUTER_BUILD_NAME, SAMPLE_GAMING_COMPUTER_BUILD_DESCRIPTION);

        String createComputerBuildURL = BASE_URL + COMPUTER_BUILD_API;

        String buildIdentifier = createComputerBuild(content, createComputerBuildURL);

        String getComputerBuildByIdentifierURL = BASE_URL + COMPUTER_BUILD_API + buildIdentifier;

        ComputerBuildDto buildContents = getComputerBuildContents(getComputerBuildByIdentifierURL, WebUtility.getEntity(null),
                HttpStatus.OK.value());

        assertNotNull(buildContents);
        assertEquals(buildIdentifier, buildContents.getBuildIdentifier());
    }

    /*
     * testing if a computer build that was created with a build identifier can be retrieved by passing in an incorrect
     * build identifier.
     */
    @Test
    public void testGetComputerBuildByIncorrectIdentifier() throws Exception {
        String content = ComputerBuildUtility.getComputerBuildAsJson(SAMPLE_BUDGET_COMPUTER_BUILD_NAME, SAMPLE_GAMING_COMPUTER_BUILD_DESCRIPTION);

        String createComputerBuildURL = BASE_URL + COMPUTER_BUILD_API;

        String buildIdentifier = createComputerBuild(content, createComputerBuildURL);

        String getComputerBuildByIdentifierURL = BASE_URL + COMPUTER_BUILD_API + buildIdentifier + INVALID_IDENTIFIER_SUFFIX;

        GenericRequestExceptionResponse response = getComputerBuildContentsWithInvalidId(getComputerBuildByIdentifierURL, WebUtility.getEntity(null),
                HttpStatus.BAD_REQUEST.value());

        assertNotNull(response.getMessage());
        assertEquals(COMPUTER_BUILD_DOES_NOT_EXIST, response.getMessage());
    }

    /*
     * testing if a computer build that was created can be deleted with the proper build identifier.
     */
    @Test
    public void testDeleteComputerBuild() throws Exception {
        String content = ComputerBuildUtility.getComputerBuildAsJson(SAMPLE_BUDGET_COMPUTER_BUILD_NAME, SAMPLE_GAMING_COMPUTER_BUILD_DESCRIPTION);

        String createComputerBuildURL = BASE_URL + COMPUTER_BUILD_API;

        String buildIdentifier = createComputerBuild(content, createComputerBuildURL);

        String deleteComputerBuildByIdentifierURL = BASE_URL + COMPUTER_BUILD_API + buildIdentifier;

        deleteComputerBuild(deleteComputerBuildByIdentifierURL,
                WebUtility.getEntityWithToken(null, token), HttpStatus.OK.value());
    }

    /*
     * testing if a computer build that was created can be deleted with the an invalid build identifier.
     */
    @Test
    public void testDeleteComputerBuildWithInvalidIdentifier() throws Exception {
        String content = ComputerBuildUtility.getComputerBuildAsJson(SAMPLE_BUDGET_COMPUTER_BUILD_NAME, SAMPLE_GAMING_COMPUTER_BUILD_DESCRIPTION);

        String createComputerBuildURL = BASE_URL + COMPUTER_BUILD_API;

        String buildIdentifier = createComputerBuild(content, createComputerBuildURL);

        String deleteComputerBuildByIdentifierURL = BASE_URL + COMPUTER_BUILD_API + buildIdentifier + INVALID_IDENTIFIER_SUFFIX;

        deleteComputerBuild(deleteComputerBuildByIdentifierURL,
                WebUtility.getEntityWithToken(null, token), HttpStatus.BAD_REQUEST.value());
    }

    /*
     * testing if a computer build that was created can be deleted by someone that is not the owner.
     */
    @Test
    public void testDeleteComputerBuildWithInvalidOwner() throws Exception {
        String content = ComputerBuildUtility.getComputerBuildAsJson(SAMPLE_BUDGET_COMPUTER_BUILD_NAME, SAMPLE_GAMING_COMPUTER_BUILD_DESCRIPTION);

        String createComputerBuildURL = BASE_URL + COMPUTER_BUILD_API;

        String buildIdentifier = createComputerBuild(content, createComputerBuildURL);

        String deleteComputerBuildByIdentifierURL = BASE_URL + COMPUTER_BUILD_API + buildIdentifier;

        // login as a different user...
        token = loginHelper(USER_NAME_TO_TEST_OWNERSHIP_ENDPOINTS, USER_PASSWORD, restTemplate);

        Object response = deleteComputerBuild(deleteComputerBuildByIdentifierURL,
                WebUtility.getEntityWithToken(null, token), HttpStatus.BAD_REQUEST.value());

        LinkedHashMap responseContent = (LinkedHashMap) response;
        assertNotNull(responseContent.get(MESSAGE_KEY));
        assertEquals(COMPUTER_BUILD_CANNOT_BE_MODIFIED, responseContent.get(MESSAGE_KEY));
    }

    /*
     * testing if all the computer builds can be retrieved.
     */
    @Test
    public void testGetAllComputerBuilds() throws Exception {
        String getAllComputerBuildsURL = BASE_URL + COMPUTER_BUILD_API;
        Object builds = getAllComputerBuilds(getAllComputerBuildsURL, WebUtility.getEntity(null));
        List<ComputerBuildDto> computerBuilds = (List<ComputerBuildDto>) builds;
//        List<ComputerBuildDto> computerBuilds = getAllComputerBuilds(getAllComputerBuildsURL, WebUtility.getEntity(null));

        // in the bootstrap class we created a computer build so we expect size that is not zero (is greater than zero).
        assertNotEquals(0, computerBuilds.size());
    }

    /*
     * testing if all the computer builds can be retrieved from a specific user that has created a computer build
     * so the size is expected to be greater than zero.
     */
    @Test
    public void testGetAllComputerBuildsFromUser() throws Exception {
        String getAllComputerBuildsURL = BASE_URL + COMPUTER_BUILD_API;
        String getAllComputersWithUserURL = getAllComputerBuildsURL + USER_NAME_REQUEST + ANOTHER_USER_NAME_TO_CREATE_NEW_USER;
        Object builds = getAllComputerBuilds(getAllComputersWithUserURL, WebUtility.getEntity(null));
        List<ComputerBuildDto> computerBuilds = (List<ComputerBuildDto>) builds;
//        List<ComputerBuildDto> computerBuilds = getAllComputerBuilds(getAllComputerBuildsURL, WebUtility.getEntity(null));

        // in the bootstrap class we created a computer build so we expect size of one.
        assertNotEquals(0, computerBuilds.size());
    }

    /*
     * testing if all the computer builds can be retrieved from a specific user that has not created any computer build
     * so the size is expected to be zero.
     */
    @Test
    public void testGetAllComputerBuildsFromUserWithNoBuilds() throws Exception {
        String getAllComputerBuildsURL = BASE_URL + COMPUTER_BUILD_API;
        String getAllComputersWithUserURL = getAllComputerBuildsURL + USER_NAME_REQUEST + SUCCESSFUL_USER_NAME;
        Object builds = getAllComputerBuilds(getAllComputersWithUserURL, WebUtility.getEntity(null));
        List<ComputerBuildDto> computerBuilds = (List<ComputerBuildDto>) builds;
        int expectedSize = 0;

        // in the bootstrap class we created a computer build so we expect size of one.
        assertEquals(expectedSize, computerBuilds.size());
    }

    /*
     * helper method to create a computer build.
     */
    private String createComputerBuild(String content, String createComputerBuildURL) throws Exception {
        ComputerBuildResponse contents = getContents(createComputerBuildURL, WebUtility.getEntityWithToken(content, token), HttpStatus.CREATED.value());
        assertNotNull(contents);
        assertEquals(SAMPLE_BUDGET_COMPUTER_BUILD_NAME, contents.getName());
        assertEquals(SAMPLE_GAMING_COMPUTER_BUILD_DESCRIPTION, contents.getBuildDescription());
        String buildIdentifier = contents.getBuildIdentifier();
        assertNotNull(buildIdentifier);
        return buildIdentifier;
    }

    /*
     * helper method to put the results of posting to an end point that will have information such as a message (such as
     * a success message), the status of if the user's account is activated (enabled), and if there is an associated
     * JWT token with the user.
     */
    private ComputerBuildResponse getContents(String url, HttpEntity<String> entity, int expectedHttpStatusCode) throws Exception{
        URI uri = new URI(url);
        ResponseEntity<ComputerBuildResponse> result = restTemplate.postForEntity(uri, entity, ComputerBuildResponse.class);
        assertNotNull(result);
        assertEquals(expectedHttpStatusCode, result.getStatusCode().value());
        return result.getBody();
    }

    private ComputerBuildDto getComputerBuildContents(String url, HttpEntity<String> entity, int expectedHttpStatusCode) throws Exception{
        URI uri = new URI(url);
        ResponseEntity<ComputerBuildDto> result = restTemplate.exchange(uri, HttpMethod.GET, entity, ComputerBuildDto.class);
        assertNotNull(result);
        assertEquals(expectedHttpStatusCode, result.getStatusCode().value());
        return result.getBody();
    }

    private GenericRequestExceptionResponse getComputerBuildContentsWithInvalidId(String url, HttpEntity<String> entity, int expectedHttpStatusCode) throws Exception{
        URI uri = new URI(url);
        ResponseEntity<GenericRequestExceptionResponse> result = restTemplate.exchange(uri, HttpMethod.GET, entity, GenericRequestExceptionResponse.class);
        assertNotNull(result);
        assertEquals(expectedHttpStatusCode, result.getStatusCode().value());
        return result.getBody();
    }

    private Object deleteComputerBuild(String url, HttpEntity<String> entity, int expectedHttpStatusCode) throws Exception {
        URI uri = new URI(url);
        ResponseEntity<Object> result = restTemplate.exchange(uri, HttpMethod.DELETE, entity, Object.class);
        assertNotNull(result);
        assertEquals(expectedHttpStatusCode, result.getStatusCode().value());
        return result.getBody();
    }

    private Object getAllComputerBuilds(String url, HttpEntity<String> entity) throws Exception {
        URI uri = new URI(url);
        ResponseEntity<Object> result = restTemplate.exchange(uri, HttpMethod.GET, entity, Object.class);
        assertNotNull(result);
        return result.getBody();
    }
}
