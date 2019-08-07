package tran.compbuildbackend.controllers.utility;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import tran.compbuildbackend.domain.utility.ApplicationUserUtility;
import tran.compbuildbackend.payload.email.JWTLoginSuccessResponse;
import tran.compbuildbackend.payload.email.LoginRequest;
import tran.compbuildbackend.security.JwtTokenProvider;
import tran.compbuildbackend.services.security.ApplicationUserAuthenticationService;

import java.net.URI;
import java.util.LinkedHashMap;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static tran.compbuildbackend.constants.mapping.MappingConstants.LOGIN_URL;
import static tran.compbuildbackend.constants.mapping.MappingConstants.USERS_API;
import static tran.compbuildbackend.constants.tests.TestUtility.BASE_URL;

public class WebUtility {

    /**
     * @param content The content to be set as JSON content.
     * @return Returns an HttpEntity object that can be used to make api calls with the entered content as JSON
     */
    public static HttpEntity<String> getEntity(String content) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(content, headers);
    }

    /**
     * @param content The content to be set as JSON content.
     * @param token The token to be put into the Authorization header.
     * @return Returns an HttpEntity object that can be used to make api calls with the entered content as JSON and
     * with a token inside of the Authorization header.
     */
    public static HttpEntity<String> getEntityWithToken(String content, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        return new HttpEntity<>(content, headers);
    }

    /**
     * helper method to post to the login URL as well as return the token from the successful login.
     * @param userName The username of the user.
     * @param password The password of the user logging in.
     * @param restTemplate An object to assist with making the POST request to the login endpoint.
     * @return The JWT token upon successful login, an exception is expected to be thrown if there is an error.
     * @throws Exception If there is an issue with posting to the login URL successfully.
     */
    public static String loginHelper(String userName, String password, TestRestTemplate restTemplate) throws Exception {
        String loginContent = ApplicationUserUtility.getLoginRequestAsJson(userName, password);
        String loginURL = BASE_URL + USERS_API + LOGIN_URL;
        URI uri = new URI(loginURL);

        ResponseEntity<Object> result = restTemplate.exchange(uri, HttpMethod.POST, WebUtility.getEntity(loginContent), Object.class);
        Object loginSuccessResponse = result.getBody();
        LinkedHashMap response = (LinkedHashMap) loginSuccessResponse;
        Gson gson = new Gson();
        String json = gson.toJson(response,LinkedHashMap.class);
        ObjectMapper objectMapper = new ObjectMapper();
        JWTLoginSuccessResponse loginResponse = objectMapper.readValue(json, JWTLoginSuccessResponse.class);

        assertNotNull(loginResponse.getToken());
        assertTrue(loginResponse.getSuccess());
        return loginResponse.getToken().substring(7);
    }

    /**
     * A helper method used in our tests in order to log the user in. This will add the authenticated user to Spring's
     * security context.
     * @param applicationUserAuthenticationService The authentication service to assist with authenticating credentials.
     * @param authenticationManager The authenticationManager to perform the authentication with the credentials.
     * @param jwtTokenProvider The jwtTokenProvider to produce a JWT token upon successful authentication.
     * @param loginRequest The payload object holding the user's credentials.
     */
    public static void logUserIn(ApplicationUserAuthenticationService applicationUserAuthenticationService, AuthenticationManager authenticationManager,
                            JwtTokenProvider jwtTokenProvider, LoginRequest loginRequest) {
        applicationUserAuthenticationService.applicationUserAuthentication(loginRequest, authenticationManager, jwtTokenProvider);
    }

    public static void logUserOut() {
        SecurityContextHolder.getContext().setAuthentication(null);
    }
}
