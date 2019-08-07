package tran.compbuildbackend.constants.exception;

import org.springframework.web.util.HtmlUtils;

public class ExceptionConstants {
    public static final int EXCEPTION_EMAIL_NAME_DOES_NOT_EXIST = 1;
    public static final int EXCEPTION_REQUEST_PASSWORD_CHANGE_FAILED = 2;

    // exception messages.
    public static final String REQUEST_IS_NULL_ERROR = "Request is null or not properly created.";
    public static final String TOKEN_HAS_EXPIRED = HtmlUtils.htmlEscape("token has expired, please request another token.");
    public static final String TOKEN_IS_NOT_PRESENT = "token is not present.";
    public static final String PASSWORD_CANNOT_BE_CHANGED = "Password cannot be changed at this time!";
    public static final String UNABLE_TO_CHANGE_PASSWORD = "unable to change password.";
    public static final String CANNOT_RETRIEVE_TOKEN = "cannot retrieve token.";
    public static final String INVALID_IDENTIFIER_FORMAT = "invalid unique identifier format.";
    public static final String CANNOT_FIND_USER = "cannot find the user";
}
