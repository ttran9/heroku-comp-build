package tran.compbuildbackend.controllers.computerbuild;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
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
import tran.compbuildbackend.domain.computerbuild.Direction;

import java.net.URI;
import java.util.LinkedHashMap;

import static org.junit.Assert.*;
import static tran.compbuildbackend.constants.computerbuild.ComputerBuildConstants.*;
import static tran.compbuildbackend.constants.fields.ErrorKeyConstants.MESSAGE_KEY;
import static tran.compbuildbackend.constants.fields.FieldConstants.ID_FIELD;
import static tran.compbuildbackend.constants.mapping.MappingConstants.DIRECTION_API;
import static tran.compbuildbackend.constants.mapping.MappingConstants.URL_SEPARATOR;
import static tran.compbuildbackend.constants.tests.TestUtility.*;
import static tran.compbuildbackend.constants.users.UserConstants.*;
import static tran.compbuildbackend.controllers.utility.WebUtility.loginHelper;
import static tran.compbuildbackend.domain.utility.ComputerBuildDetailUtility.*;
import static tran.compbuildbackend.domain.utility.DirectionUtility.getDirectionAsJson;

@Profile("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class DirectionControllerTest {

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
     * testing a direction creation passing in required fields and logged in as the owner.
     */
    @Test
    public void createDirectionSuccess() throws Exception {
        generateDirection();
    }

    /*
     * testing to create a direction a computer build but not as the owner.
     */
    @Test
    public void createDirectionNotAsOwner() throws Exception {
        LinkedHashMap response = generateDirectionFailure(TEST_DIRECTION_DESCRIPTION, true);
        assertNotNull(response.get(MESSAGE_KEY));
        assertEquals(COMPUTER_BUILD_CANNOT_BE_MODIFIED, response.get(MESSAGE_KEY));
    }

    /*
     * testing a direction creation without including a description (required field).
     */
    @Test
    public void createDirectionFailure() throws Exception {
        LinkedHashMap response = generateDirectionFailure(null, false);
        assertNotNull(response.get(DESCRIPTION_KEY));
        assertEquals(FIELD_CANNOT_BE_NULL, response.get(DESCRIPTION_KEY));
    }

    /*
     * testing a direction update passing in required fields and logged in as the owner.
     */
    @Test
    public void updateDirectionSuccess() throws Exception {
        Direction response = (Direction) updateDirection(false, true, TEST_DIRECTION_DESCRIPTION_UPDATED);
        assertNotNull(response);
        assertNotEquals(TEST_DIRECTION_DESCRIPTION, response.getDescription());
        assertEquals(TEST_DIRECTION_DESCRIPTION_UPDATED, response.getDescription());
    }

    /*
     * testing a direction update on a computer build but not as the owner.
     */
    @Test
    public void updateDirectionNotAsOwner() throws Exception {
        LinkedHashMap response = (LinkedHashMap) updateDirection(true, false, TEST_DIRECTION_DESCRIPTION_UPDATED);
        assertNotNull(response);
        assertNotNull(response.get(MESSAGE_KEY));
        assertEquals(COMPUTER_BUILD_CANNOT_BE_MODIFIED, response.get(MESSAGE_KEY));
    }

    /*
     * testing a direction update without including a description (required field).
     */
    @Test
    public void updateDirectionFailure() throws Exception {
        LinkedHashMap response = (LinkedHashMap) updateDirection(false, false, null);
        assertNotNull(response);
        assertNotNull(response.get(DESCRIPTION_KEY));
        assertEquals(FIELD_CANNOT_BE_NULL, response.get(DESCRIPTION_KEY));
    }

    /*
     * testing a successful direction deletion.
     */
    @Test
    public void deleteDirectionSuccess() throws Exception {
        LinkedHashMap contents = createDirectionRequest(false, HttpMethod.DELETE, HttpStatus.OK.value(),
                true, true);
        assertNotNull(contents.get(MESSAGE_KEY));
        assertEquals(contents.get(MESSAGE_KEY), DIRECTION_DELETE_MESSAGE);
    }

    /*
     * testing a direction deletion where the user logged in is not the owner so the deletion cannot be done.
     */
    @Test
    public void deleteDirectionNotAsOwner() throws Exception {
        LinkedHashMap contents = createDirectionRequest(true, HttpMethod.DELETE, HttpStatus.BAD_REQUEST.value(),
                true, true);
        assertNotNull(contents.get(MESSAGE_KEY));
        assertEquals(contents.get(MESSAGE_KEY), COMPUTER_BUILD_CANNOT_BE_MODIFIED);
    }

    /*
     * testing a direction deletion where the user logged in has entered in the incorrect identifier so the direction
     * is not deleted.
     */
    @Test
    public void deleteDirectionFailure() throws Exception {
        LinkedHashMap contents = createDirectionRequest(false, HttpMethod.DELETE, HttpStatus.BAD_REQUEST.value(),
                true, false);
        assertNotNull(contents.get(MESSAGE_KEY));
        assertEquals(contents.get(MESSAGE_KEY), DIRECTION_CANNOT_BE_DELETED);
    }

    /*
     * testing when the user is logged in and attempts to retrieve a direction by the correct identifier.
     */
    @Test
    public void getDirectionLoggedInSuccess() throws Exception {
        LinkedHashMap contents = createDirectionRequest(false, HttpMethod.GET, HttpStatus.OK.value(),
                true, true);
        assertEquals(TEST_DIRECTION_DESCRIPTION, contents.get(DESCRIPTION_KEY));
        assertNotNull(contents.get(UNIQUE_IDENTIFIER_KEY));
        assertNotNull(contents.get(ID_FIELD));
    }

    /*
     * testing when the user is not logged in and attempts to retrieve a direction by the correct identifier.
     */
    @Test
    public void getDirectionNotLoggedInSuccess() throws Exception {
        LinkedHashMap contents = createDirectionRequest(false, HttpMethod.GET, HttpStatus.OK.value(),
                false, true);
        assertEquals(TEST_DIRECTION_DESCRIPTION, contents.get(DESCRIPTION_KEY));
        assertNotNull(contents.get(UNIQUE_IDENTIFIER_KEY));
        assertNotNull(contents.get(ID_FIELD));
    }

    /*
     * testing when the user is logged in but provides the incorrect identifier when attempting to retrieve a direction.
     */
    @Test
    public void getDirectionLoggedInFailure() throws Exception {
        LinkedHashMap contents = createDirectionRequest(false, HttpMethod.GET, HttpStatus.BAD_REQUEST.value(),
                true, false);
        assertNotNull(contents);
        assertEquals(INVALID_DIRECTION, contents.get(MESSAGE_KEY));
    }

    /*
     * testing when the user is not logged in and provides the incorrect identifier when attempting to retrieve a direction.
     */
    @Test
    public void getDirectionNotLoggedInFailure() throws Exception {
        LinkedHashMap contents = createDirectionRequest(false, HttpMethod.GET, HttpStatus.BAD_REQUEST.value(),
                false, false);
        assertNotNull(contents);
        assertEquals(INVALID_DIRECTION, contents.get(MESSAGE_KEY));
    }

    /*
     * helper method to create a direction and verify some of its fields.
     */
    private Direction createDirection(String content, String createDirectionBuildURL, int expectedStatusCode) throws Exception {
        Object response = createRequest(createDirectionBuildURL, WebUtility.getEntityWithToken(content, token), expectedStatusCode,
                HttpMethod.POST, restTemplate);
        LinkedHashMap contents = (LinkedHashMap) response;
        String json = new Gson().toJson(contents,LinkedHashMap.class);
        Direction direction = new ObjectMapper().readValue(json, Direction.class);

        assertEquals(TEST_DIRECTION_DESCRIPTION, direction.getDescription());
        assertNotNull(direction.getId());
        assertNotNull(direction.getUniqueIdentifier());
        return direction;
    }

    /*
     * helper method to create a patch request that is successful and returns a direction.
     */
    private Direction updateRequestSuccess(String url, HttpEntity<String> entity) throws Exception {
        URI uri = new URI(url);
        return restTemplate.patchForObject(uri, entity, Direction.class);
    }

    /*
     * helper method to persist/create a direction object for testing.
     */
    private Direction generateDirection() throws Exception {
        String content = getDirectionAsJson(TEST_DIRECTION_DESCRIPTION);
        String createDirectionURL = BASE_URL + DIRECTION_API + computerBuildIdentifier;
        return createDirection(content, createDirectionURL, HttpStatus.CREATED.value());
    }

    /*
     * helper method to create a direction when either not the owner or passing in invalid data.
     */
    private LinkedHashMap generateDirectionFailure(String description, boolean logInSecondTime) throws Exception {
        if(logInSecondTime) {
            // login as someone else.
            token = loginHelper(ANOTHER_USER_NAME_TO_CREATE_NEW_USER, USER_PASSWORD, restTemplate);
        }

        String content = getDirectionAsJson(description);
        String createDirectionURL = BASE_URL + DIRECTION_API + computerBuildIdentifier;
        return createComputerDetailFailure(content, createDirectionURL, HttpStatus.BAD_REQUEST.value(),
                token, restTemplate);
    }

    /*
     * helper method to update a direction for successful and failing cases.
     */
    private Object updateDirection(boolean logInSecondTime, boolean isSuccessfulUpdate, String description) throws Exception {
        Direction direction = generateDirection();

        if(logInSecondTime) {
            // log in as someone else..
            token = loginHelper(ANOTHER_USER_NAME_TO_CREATE_NEW_USER, USER_PASSWORD, restTemplate);
        }

        String directionUniqueIdentifier = direction.getUniqueIdentifier();
        String updateDirectionURL = BASE_URL + DIRECTION_API + computerBuildIdentifier + URL_SEPARATOR + directionUniqueIdentifier;
        // update the direction that was created above.
        String content = getDirectionAsJson(description, computerBuildIdentifier, direction.getId(), directionUniqueIdentifier);

        return isSuccessfulUpdate ? updateRequestSuccess(updateDirectionURL, WebUtility.getEntityWithToken(content, token)) :
                updateRequest(updateDirectionURL, WebUtility.getEntityWithToken(content, token), restTemplate);
    }

    /*
     * helper method to get or delete a direction.
     */
    private LinkedHashMap createDirectionRequest(boolean logInSecondTime, HttpMethod httpMethod, int expectedStatusCode,
                                                 boolean loggedIn, boolean isSuccess) throws Exception {
        Direction direction = generateDirection();

        if(logInSecondTime) {
            token = loginHelper(ANOTHER_USER_NAME_TO_CREATE_NEW_USER, USER_PASSWORD, restTemplate);
        }
        String directionUniqueIdentifier = direction.getUniqueIdentifier();
        String url = isSuccess ? BASE_URL + DIRECTION_API + computerBuildIdentifier + URL_SEPARATOR + directionUniqueIdentifier :
                BASE_URL + DIRECTION_API + computerBuildIdentifier + URL_SEPARATOR + directionUniqueIdentifier + INVALID_IDENTIFIER_SUFFIX;

        Object response = loggedIn ? createRequest(url, WebUtility.getEntityWithToken(null, token), expectedStatusCode, httpMethod, restTemplate) :
                createRequest(url, WebUtility.getEntity(null), expectedStatusCode, httpMethod, restTemplate);

        assertNotNull(response);
        LinkedHashMap contents = (LinkedHashMap) response;
        assertNotNull(contents);
        return contents;
    }
}
