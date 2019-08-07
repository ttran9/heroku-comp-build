package tran.compbuildbackend.domain.utility;

import com.google.gson.Gson;
import tran.compbuildbackend.domain.user.ApplicationUser;
import tran.compbuildbackend.payload.email.InitialPasswordChangeRequest;
import tran.compbuildbackend.payload.email.LoginRequest;
import tran.compbuildbackend.payload.email.PasswordChangeRequest;

public class ApplicationUserUtility {

    public static String getUserAsJson(String userName, String email, String fullName, String password, String confirmPassword) {
        ApplicationUser user = new ApplicationUser(userName, email, fullName, password, confirmPassword);
        return new Gson().toJson(user);
    }

    public static String getUserAsJson(String userName, String email, String password, String confirmPassword) {
        ApplicationUser user = new ApplicationUser(userName, email, password, confirmPassword);
        return new Gson().toJson(user);
    }

    public static String getLoginRequestAsJson(String userName, String password) {
        LoginRequest loginRequest = new LoginRequest(userName, password);
        return new Gson().toJson(loginRequest);
    }

    public static String getInitialPasswordChangeRequestAsJson(String userName) {
        InitialPasswordChangeRequest passwordChangeRequest = new InitialPasswordChangeRequest(userName);
        return new Gson().toJson(passwordChangeRequest);
    }

    public static String getPasswordChangeRequestAsJson(String password, String confirmPassword) {
        PasswordChangeRequest passwordChangeRequest = new PasswordChangeRequest(password, confirmPassword);
        return new Gson().toJson(passwordChangeRequest);
    }


}
