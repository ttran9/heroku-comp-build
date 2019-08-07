package tran.compbuildbackend.services.security;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import tran.compbuildbackend.payload.email.LoginRequest;
import tran.compbuildbackend.security.JwtTokenProvider;

public interface ApplicationUserAuthenticationService {

    /**
     * @param loginRequest The payload coming in from the user form, such as entered username and password.
     * @param authenticationManager An object from spring security to perform the authentication against our database.
     * @param jwtTokenProvider An object to help generate the token.
     * @return Returns a custom object with the JWT token if authentication is successful and a variety of exceptions are thrown
     * depending on the error related to authentication.
     */
    ResponseEntity<?> applicationUserAuthentication(LoginRequest loginRequest, AuthenticationManager authenticationManager,
                                                    JwtTokenProvider jwtTokenProvider);
}
