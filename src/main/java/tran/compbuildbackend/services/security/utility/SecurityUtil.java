package tran.compbuildbackend.services.security.utility;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import tran.compbuildbackend.domain.security.VerificationToken;
import tran.compbuildbackend.domain.user.ApplicationUser;

import java.time.LocalDateTime;

import static tran.compbuildbackend.constants.exception.ExceptionConstants.TOKEN_HAS_EXPIRED;
import static tran.compbuildbackend.constants.security.SecurityConstants.CHANGE_PASSWORD_TOKEN_TYPE;
import static tran.compbuildbackend.constants.security.SecurityConstants.EMAIL_VERIFICATION_TOKEN_TYPE;
import static tran.compbuildbackend.exceptions.ExceptionUtility.throwPasswordException;
import static tran.compbuildbackend.exceptions.ExceptionUtility.throwTokenException;

public class SecurityUtil {
    /*
     * helper method to grab the user name from the logged in user.
     * This will be called from a service that has gone through JwtAuthenticationFilter and it is expected that the
     * authentication object has been set which will hold an instance of the ApplicationUser.
     */
    public static ApplicationUser getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null) {
            Object principal = authentication.getPrincipal();
            if(principal != null) {
                if(principal instanceof ApplicationUser) {
                    return (ApplicationUser) principal;
                }
            }
        }
        throw new UsernameNotFoundException("cannot retrieve the logged in user.");
    }

    /**
     * Helper method to check if a token has expired.
     * @param token The token to be verified.
     * @param tokenType The type of custom exception to be thrown.
     */
    public static void isTokenExpired(VerificationToken token, int tokenType) {
        // verify if the token is expired.
        LocalDateTime currentTime = LocalDateTime.now();
        if(currentTime.isAfter(token.getExpirationDate())) {
            switch(tokenType) {
                case CHANGE_PASSWORD_TOKEN_TYPE:
                    /*
                     * originally the custom exception had fields "password" and "confirm password" showing the same error
                     * so instead this will only display the error on one field, the "password" field.
                     */
                    throwPasswordException(TOKEN_HAS_EXPIRED);
                case EMAIL_VERIFICATION_TOKEN_TYPE:
                    throwTokenException(TOKEN_HAS_EXPIRED);
                default:
                    break;
            }
        }
    }

}
