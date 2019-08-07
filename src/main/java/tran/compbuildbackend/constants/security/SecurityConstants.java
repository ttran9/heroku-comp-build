package tran.compbuildbackend.constants.security;

public class SecurityConstants {
    // endpoints
    public static final String SIGN_UP_URLS = "/api/users/**";
    public static final String H2_URL = "/h2-console/**";

    // JWT authentication related content
    public static final String SECRET = System.getenv("JWT_PRIV_KEY"); // TODO: will refactor this with a more secure implementation
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final long EXPIRATION_TIME = 2700000; // in milliseconds so 2700 seconds / 45 mins.

    // email credentials
    public static final String EMAIL_LOGIN = System.getenv("TEST_EMAIL"); // in milliseconds so 2700 seconds / 45 mins.
    public static final String EMAIL_PASSWORD = System.getenv("TEST_PASSWORD"); // in milliseconds so 2700 seconds / 45 mins.

    // constants for token verification.
    public static final int CHANGE_PASSWORD_TOKEN_TYPE = 1;
    public static final int EMAIL_VERIFICATION_TOKEN_TYPE = 2;
}
