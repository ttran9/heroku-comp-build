package tran.compbuildbackend.services.security;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import tran.compbuildbackend.payload.email.JWTLoginSuccessResponse;
import tran.compbuildbackend.payload.email.LoginRequest;
import tran.compbuildbackend.security.JwtTokenProvider;

import static tran.compbuildbackend.constants.security.SecurityConstants.TOKEN_PREFIX;

@Service
public class ApplicationUserAuthenticationServiceImpl implements ApplicationUserAuthenticationService {
    /**
     * @param loginRequest The payload coming in from the user form, such as entered username and password.
     * @param authenticationManager An object from spring security to perform the authentication against our database.
     * @param jwtTokenProvider An object to help generate the token.
     * @return Returns a custom object with the JWT token if authentication is successful and a variety of exceptions are thrown
     * depending on the error related to authentication.
     */
    @Override
    public ResponseEntity<?> applicationUserAuthentication(LoginRequest loginRequest, AuthenticationManager authenticationManager,
                                                           JwtTokenProvider jwtTokenProvider) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwtToken = TOKEN_PREFIX + jwtTokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new JWTLoginSuccessResponse(true, jwtToken));
    }
}
