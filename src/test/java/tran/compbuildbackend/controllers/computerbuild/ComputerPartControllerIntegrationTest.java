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
import tran.compbuildbackend.domain.computerbuild.ComputerPart;
import tran.compbuildbackend.domain.utility.ComputerPartUtility;

import java.net.URI;
import java.util.LinkedHashMap;

import static org.junit.Assert.*;
import static tran.compbuildbackend.constants.computerbuild.ComputerBuildConstants.*;
import static tran.compbuildbackend.constants.fields.ErrorKeyConstants.MESSAGE_KEY;
import static tran.compbuildbackend.constants.fields.FieldConstants.ID_FIELD;
import static tran.compbuildbackend.constants.fields.FieldValueConstants.PRICE_INCORRECT_FORMAT;
import static tran.compbuildbackend.constants.fields.FieldValueConstants.PRICE_INCORRECT_RANGE;
import static tran.compbuildbackend.constants.mapping.MappingConstants.COMPUTER_PART_API;
import static tran.compbuildbackend.constants.mapping.MappingConstants.URL_SEPARATOR;
import static tran.compbuildbackend.constants.tests.TestUtility.*;
import static tran.compbuildbackend.constants.users.UserConstants.*;
import static tran.compbuildbackend.controllers.utility.WebUtility.loginHelper;
import static tran.compbuildbackend.domain.utility.ComputerBuildDetailUtility.*;
import static tran.compbuildbackend.domain.utility.DateUtility.convertDateToString;

@Profile("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class ComputerPartControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private String token;

    private String computerBuildIdentifier;

    @Before
    public void setUp() throws Exception {
        token = loginHelper(USER_NAME_TO_TEST_OWNERSHIP_ENDPOINTS_CONTROLLERS, USER_PASSWORD, restTemplate);
        LinkedHashMap contents = getComputerBuildIdentifier(restTemplate, USER_NAME_TO_TEST_OWNERSHIP_ENDPOINTS_CONTROLLERS);
        computerBuildIdentifier = (String) contents.get(BUILD_IDENTIFIER_KEY);
    }

    /*
     * testing a successful computer part creation (happy path)
     */
    @Test
    public void testCreateComputerPartSuccess() throws Exception {
        generateComputerPart(TEST_COMPUTER_PART_PRICE);
    }

    /*
     * testing a computer part creation where the user is not the owner so it is expected that the computer part
     * that is returned will have null fields and was not created.
     */
    @Test
    public void testCreateComputerPartNotAsOwner() throws Exception {
        LinkedHashMap contents = generateComputerDetailFailure(TEST_COMPUTER_PART_PLACE_PURCHASED_AT,true,
                TEST_COMPUTER_PART_PRICE);
        assertNotNull(contents.get(MESSAGE_KEY));
        assertEquals(COMPUTER_BUILD_CANNOT_BE_MODIFIED, contents.get(MESSAGE_KEY));
    }

    /*
     * testing a computer part creation where the user is the owner but there is a field that was not filled out and the
     * returned computer part will have null fields and was not created.
     */
    @Test
    public void testCreateComputerPartFailure() throws Exception {
        LinkedHashMap contents = generateComputerDetailFailure(null,false, TEST_COMPUTER_PART_PRICE);
        assertNotNull(contents.get(PLACE_PURCHASED_AT_KEY));
        assertEquals(FIELD_CANNOT_BE_EMPTY, contents.get(PLACE_PURCHASED_AT_KEY));
    }

    /*
     * testing a computer part creation where the user is the owner but the price field is invalid because it is nut
     * of the accepted range.
     */
    @Test
    public void testCreateComputerPartFailurePriceTooHigh() throws Exception {
        LinkedHashMap contents = generateComputerDetailFailure(TEST_COMPUTER_PART_PLACE_PURCHASED_AT,false,
                TEST_COMPUTER_PART_PRICE_TOO_HIGH);
        assertNotNull(contents.get(PRICE_KEY));
        assertEquals(PRICE_INCORRECT_RANGE, contents.get(PRICE_KEY));
    }

    /*
     * testing a computer part creation where the user is the owner but the price field is invalid because it is nut
     * properly formatted
     */
    @Test
    public void testCreateComputerPartFailureImproperFormat() throws Exception {
        LinkedHashMap contents = generateComputerDetailFailure(TEST_COMPUTER_PART_PLACE_PURCHASED_AT,false,
                TEST_COMPUTER_PART_PRICE_IMPROPER_FORMAT);
        assertNotNull(contents.get(PRICE_KEY));
        assertEquals(PRICE_INCORRECT_FORMAT, contents.get(PRICE_KEY));
}

    /*
     * testing a successful computer part upgrade where there is a price update.
     */
    @Test
    public void upgradeComputerPartPriceUpdateSuccess() throws Exception {
        ComputerPart response = (ComputerPart) updateComputerPart(false, true, TEST_COMPUTER_PART_PLACE_PURCHASED_AT_TWO,
                TEST_COMPUTER_PART_PRICE, TEST_COMPUTER_PART_PRICE_TWO);
        assertNotNull(response);
        assertNotEquals(TEST_COMPUTER_PART_PLACE_PURCHASED_AT, response.getPlacePurchasedAt());
        assertEquals(TEST_COMPUTER_PART_PLACE_PURCHASED_AT_TWO, response.getPlacePurchasedAt());
    }

    /*
     * testing a successful computer part upgrade where there is no price update.
     */
    @Test
    public void upgradeComputerPartNoPriceUpdateSuccess() throws Exception {
        ComputerPart response = (ComputerPart) updateComputerPart(false, true, TEST_COMPUTER_PART_PLACE_PURCHASED_AT_TWO,
                TEST_COMPUTER_PART_PRICE, TEST_COMPUTER_PART_PRICE);
        assertNotNull(response);
        assertNotEquals(TEST_COMPUTER_PART_PLACE_PURCHASED_AT, response.getPlacePurchasedAt());
        assertEquals(TEST_COMPUTER_PART_PLACE_PURCHASED_AT_TWO, response.getPlacePurchasedAt());
    }

    /*
     * testing a computer part upgrade when the user logged in is not the owner so the computer part cannot be updated.
     */
    @Test
    public void upgradeComputerPartNotAsOwner() throws Exception {
        LinkedHashMap responseContent = (LinkedHashMap) updateComputerPart(true, false, TEST_COMPUTER_PART_PLACE_PURCHASED_AT_TWO,
                TEST_COMPUTER_PART_PRICE, TEST_COMPUTER_PART_PRICE_TWO);
        assertNotNull(responseContent);
        assertNotNull(responseContent.get(MESSAGE_KEY));
        assertEquals(COMPUTER_BUILD_CANNOT_BE_MODIFIED, responseContent.get(MESSAGE_KEY));
    }

    /*
     * testing a computer part upgrade when the user does not enter in a required field so there is an error message displayed.
     */
    @Test
    public void upgradeComputerPartFailure() throws Exception {
        LinkedHashMap responseContent = (LinkedHashMap) updateComputerPart(false, false, null,
                TEST_COMPUTER_PART_PRICE, TEST_COMPUTER_PART_PRICE_TWO);
        assertNotNull(responseContent);
        assertNotNull(responseContent.get(PLACE_PURCHASED_AT_KEY));
        assertEquals(FIELD_CANNOT_BE_EMPTY, responseContent.get(PLACE_PURCHASED_AT_KEY));
    }

    /*
     * testing a successful computer part deletion.
     */
    @Test
    public void deleteComputerPartSuccess() throws Exception {
        LinkedHashMap contents = createComputerPartRequest(false, HttpMethod.DELETE, HttpStatus.OK.value(),
                true, true, TEST_COMPUTER_PART_PRICE);
        assertNotNull(contents.get(MESSAGE_KEY));
        assertEquals(contents.get(MESSAGE_KEY), COMPUTER_PART_DELETE_MESSAGE);
    }

    /*
     * testing a computer part deletion where the user logged in is not the owner so the deletion cannot be done.
     */
    @Test
    public void deleteComputerPartNotAsOwner() throws Exception {
        LinkedHashMap contents = createComputerPartRequest(true, HttpMethod.DELETE, HttpStatus.BAD_REQUEST.value(),
                true, false, TEST_COMPUTER_PART_PRICE);
        assertNotNull(contents.get(MESSAGE_KEY));
        assertEquals(contents.get(MESSAGE_KEY), COMPUTER_BUILD_CANNOT_BE_MODIFIED);
    }

    /*
     * testing a computer part deletion where the user logged in has entered in the incorrect identifier so the computer part
     * is not deleted.
     */
    @Test
    public void deleteComputerPartFailure() throws Exception {
        LinkedHashMap contents = createComputerPartRequest(false, HttpMethod.DELETE, HttpStatus.BAD_REQUEST.value(),
                true, false, TEST_COMPUTER_PART_PRICE);
        assertNotNull(contents.get(MESSAGE_KEY));
        assertEquals(contents.get(MESSAGE_KEY), COMPUTER_PART_CANNOT_BE_DELETED);
    }

    /*
     * testing when the user is logged in and attempts to retrieve a computer part by the correct identifier.
     */
    @Test
    public void getComputerPartLoggedInSuccess() throws Exception {
        LinkedHashMap contents = createComputerPartRequest(false, HttpMethod.GET, HttpStatus.OK.value(),
                true, true, TEST_COMPUTER_PART_PRICE);
        assertEquals(TEST_COMPUTER_PART_NAME, contents.get(NAME_KEY));
        assertEquals(TEST_COMPUTER_PART_PURCHASE_DATE, contents.get(PURCHASE_DATE_KEY));
        assertEquals(TEST_COMPUTER_PART_PLACE_PURCHASED_AT, contents.get(PLACE_PURCHASED_AT_KEY));
        assertEquals(TEST_COMPUTER_PART_OTHER_NOTES, contents.get(OTHER_NOTES_KEY));
        assertEquals(TEST_COMPUTER_PART_PRICE, Double.valueOf(contents.get(PRICE_KEY).toString()), 0);
        assertNotNull(contents.get(UNIQUE_IDENTIFIER_KEY));
        assertNotNull(contents.get(ID_FIELD));
    }

    /*
     * testing when the user is not logged in and attempts to retrieve a computer part by the correct identifier.
     */
    @Test
    public void getComputerPartNotLoggedInSuccess() throws Exception {
        LinkedHashMap contents = createComputerPartRequest(false, HttpMethod.GET, HttpStatus.OK.value(),
                false, true, TEST_COMPUTER_PART_PRICE);
        assertEquals(TEST_COMPUTER_PART_NAME, contents.get(NAME_KEY));
        assertEquals(TEST_COMPUTER_PART_PURCHASE_DATE, contents.get(PURCHASE_DATE_KEY));
        assertEquals(TEST_COMPUTER_PART_PLACE_PURCHASED_AT, contents.get(PLACE_PURCHASED_AT_KEY));
        assertEquals(TEST_COMPUTER_PART_OTHER_NOTES, contents.get(OTHER_NOTES_KEY));
        assertEquals(TEST_COMPUTER_PART_PRICE, Double.valueOf(contents.get(PRICE_KEY).toString()), 0);
        assertNotNull(contents.get(UNIQUE_IDENTIFIER_KEY));
        assertNotNull(contents.get(ID_FIELD));
    }

    /*
     * testing when the user is logged in but provides the incorrect identifier when attempting to retrieve
     * a computer part.
     */
    @Test
    public void getComputerPartLoggedInFailure() throws Exception {
        LinkedHashMap contents = createComputerPartRequest(false, HttpMethod.GET, HttpStatus.BAD_REQUEST.value(),
                true, false, TEST_COMPUTER_PART_PRICE);
        assertEquals(INVALID_COMPUTER_PART, contents.get(MESSAGE_KEY));
    }

    /*
     * testing when the user is not logged in and provides the incorrect identifier when attempting to retrieve
     * a computer part.
     */
    @Test
    public void getComputerPartNotLoggedInFailure() throws Exception {
        LinkedHashMap contents = createComputerPartRequest(false, HttpMethod.GET, HttpStatus.BAD_REQUEST.value(),
                false, false, TEST_COMPUTER_PART_PRICE);
        assertEquals(INVALID_COMPUTER_PART, contents.get(MESSAGE_KEY));
    }

    /*
     * helper method to create a patch request that is successful and returns a computer part.
     */
    private ComputerPart updateRequestSuccess(String url, HttpEntity<String> entity) throws Exception {
        URI uri = new URI(url);
        return restTemplate.patchForObject(uri, entity, ComputerPart.class);
    }

    /*
     * helper method to create a computer part and verify some of its fields.
     */
    private ComputerPart createComputerPart(String content, String createComputerBuildURL, int expectedStatusCode) throws Exception {
        double priceBeforeInsertion = getComputerBuildTotalPrice();
        Object response = createRequest(createComputerBuildURL, WebUtility.getEntityWithToken(content, token),
                expectedStatusCode, HttpMethod.POST, restTemplate);
        LinkedHashMap contents = (LinkedHashMap) response;
        Gson gson = new Gson();
        String json = gson.toJson(contents,LinkedHashMap.class);
        ObjectMapper objectMapper = new ObjectMapper();
        ComputerPart computerPart = objectMapper.readValue(json, ComputerPart.class);
        double priceAfterInsertion = getComputerBuildTotalPrice();

        assertEquals(TEST_COMPUTER_PART_NAME, computerPart.getName());
        assertEquals(TEST_COMPUTER_PART_PURCHASE_DATE, convertDateToString(computerPart.getPurchaseDate()));
        assertEquals(TEST_COMPUTER_PART_PLACE_PURCHASED_AT, computerPart.getPlacePurchasedAt());
        assertEquals(TEST_COMPUTER_PART_OTHER_NOTES, computerPart.getOtherNote());
        assertEquals(TEST_COMPUTER_PART_PRICE, computerPart.getPrice(), 0);
        assertNotNull(computerPart.getId());
        assertNotNull(computerPart.getUniqueIdentifier());
        assertNotEquals(priceBeforeInsertion, priceAfterInsertion);
       return computerPart;
    }

    /*
     * helper method to persist/create a computer part object for testing.
     */
    private ComputerPart generateComputerPart(double price) throws Exception {
        String content = ComputerPartUtility.getComputerPartAsJson(TEST_COMPUTER_PART_NAME, TEST_COMPUTER_PART_PURCHASE_DATE,
                TEST_COMPUTER_PART_PLACE_PURCHASED_AT, TEST_COMPUTER_PART_OTHER_NOTES, price);
        String createComputerPartBuildURL = BASE_URL + COMPUTER_PART_API + computerBuildIdentifier;
        return createComputerPart(content, createComputerPartBuildURL, HttpStatus.CREATED.value());
    }

    /*
     * helper method to create a computer part with some form of error.
     * one example would be where the user is not the owner.
     * another example would be where the user is the owner but passes in invalid data.
     */
    private LinkedHashMap generateComputerDetailFailure(String placePurchasedAt, boolean logInSecondTime,
                                                        double computerPartPrice) throws Exception {
        String content = ComputerPartUtility.getComputerPartAsJson(TEST_COMPUTER_PART_NAME, TEST_COMPUTER_PART_PURCHASE_DATE,
                placePurchasedAt, TEST_COMPUTER_PART_OTHER_NOTES, computerPartPrice);
        String createComputerPartBuildURL = BASE_URL + COMPUTER_PART_API + computerBuildIdentifier;
        if(logInSecondTime) {
            // log in as someone else..
            token = loginHelper(ANOTHER_USER_NAME_TO_CREATE_NEW_USER, USER_PASSWORD, restTemplate);
        }

        return createComputerDetailFailure(content, createComputerPartBuildURL, HttpStatus.BAD_REQUEST.value(),
                token, restTemplate);
    }

    /*
     * helper method to update a computer part for successful and failing cases.
     */
    private Object updateComputerPart(boolean logInSecondTime, boolean isSuccessfulUpdate, String placePurchasedAt,
                                      double price, double newPrice) throws Exception {
        ComputerPart computerPart = generateComputerPart(price);

        // grab old computer build by identifier and get the price.
        // this will be the expected price (which should change when we check it if we have a successful deletion).
        double priceBeforeUpdate = getComputerBuildTotalPrice();

        if(logInSecondTime) {
            // log in as someone else..
            token = loginHelper(ANOTHER_USER_NAME_TO_CREATE_NEW_USER, USER_PASSWORD, restTemplate);
        }

        String computerPartIdentifier = computerPart.getUniqueIdentifier();
        String updateComputerPartURL = BASE_URL + COMPUTER_PART_API + computerBuildIdentifier + URL_SEPARATOR + computerPartIdentifier;
        // update the computer part that was created above.
        String content = ComputerPartUtility.getComputerPartAsJson(TEST_COMPUTER_PART_NAME, TEST_COMPUTER_PART_PURCHASE_DATE,
                placePurchasedAt, TEST_COMPUTER_PART_OTHER_NOTES, newPrice,
                computerBuildIdentifier, computerPart.getId(), computerPartIdentifier);

        if(isSuccessfulUpdate) {
            ComputerPart updatedComputerPart = updateRequestSuccess(updateComputerPartURL, WebUtility.getEntityWithToken(content, token));
            double priceAfterUpdate = getComputerBuildTotalPrice();
            if(price != newPrice) {
                assertNotEquals(priceBeforeUpdate, priceAfterUpdate);
            }
            else {
                assertEquals(priceBeforeUpdate, priceAfterUpdate, EXPECTED_DIFFERENCE);
            }
            return updatedComputerPart;
        }
         else {
            return updateRequest(updateComputerPartURL, WebUtility.getEntityWithToken(content, token), restTemplate);
        }
//        return isSuccessfulUpdate ? updateRequestSuccess(updateComputerPartURL, WebUtility.getEntityWithToken(content, token)) :
//            updateRequest(updateComputerPartURL, WebUtility.getEntityWithToken(content, token), restTemplate);
    }

    /*
     * helper method to get or delete a computer part.
     */
    private LinkedHashMap createComputerPartRequest(boolean logInSecondTime, HttpMethod httpMethod, int expectedStatusCode,
                                                 boolean loggedIn, boolean isSuccess, double price) throws Exception {
        ComputerPart computerPart = generateComputerPart(price);

        if(logInSecondTime) {
            token = loginHelper(ANOTHER_USER_NAME_TO_CREATE_NEW_USER, USER_PASSWORD, restTemplate);
        }
        double priceBeforeDeletion = getComputerBuildTotalPrice();
        String uniqueIdentifier = computerPart.getUniqueIdentifier();
        String url = isSuccess ? BASE_URL + COMPUTER_PART_API + computerBuildIdentifier + URL_SEPARATOR + uniqueIdentifier :
                BASE_URL + COMPUTER_PART_API + computerBuildIdentifier + URL_SEPARATOR + uniqueIdentifier + INVALID_IDENTIFIER_SUFFIX;

        Object response = loggedIn ? createRequest(url, WebUtility.getEntityWithToken(null, token), expectedStatusCode, httpMethod, restTemplate) :
                createRequest(url, WebUtility.getEntity(null), expectedStatusCode, httpMethod, restTemplate);
        assertNotNull(response);
        LinkedHashMap contents = (LinkedHashMap) response;
        assertNotNull(contents);

        /*
         * if successful delete grab the new price and compare it to the old/expected price and make sure they are not the same
         * UNLESS the old part does not cost anything which is possible if someone got it for free such as through a friend.
         */
        if(isSuccess && httpMethod == HttpMethod.DELETE) {
            double priceAfterDeletion = getComputerBuildTotalPrice();
            assertNotEquals(priceBeforeDeletion, priceAfterDeletion);
        }

        return contents;
    }

    private double getComputerBuildTotalPrice() throws Exception{
        LinkedHashMap newComputerBuildResponse = getComputerBuildIdentifier(restTemplate, USER_NAME_TO_TEST_OWNERSHIP_ENDPOINTS_CONTROLLERS);
        return (Double) newComputerBuildResponse.get(TOTAL_PRICE_KEY);
    }
}
