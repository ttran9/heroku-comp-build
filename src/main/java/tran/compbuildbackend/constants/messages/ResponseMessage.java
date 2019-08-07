package tran.compbuildbackend.constants.messages;

import org.springframework.web.util.HtmlUtils;

public class ResponseMessage {

    public static final String VERIFY_EMAIL_MESSAGE = HtmlUtils.htmlEscape("Before logging in you must activate " +
            "your account. Please check your e-mail account.");
    public static final String SUCCESSFUL_ACCOUNT_ACTIVATION = HtmlUtils.htmlEscape("account is activated!!");
    public static final String PASSWORD_CHANGED = HtmlUtils.htmlEscape("password has been changed!");
    public static final String CHANGE_PASSWORD = HtmlUtils.htmlEscape("check your email for a link to change your password");
}
