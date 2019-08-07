package tran.compbuildbackend.payload.email;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * This object is used when the user enters in their username (e-mail) to have a URL sent to them in order to be able to
 * view a web page that allows them to change to a new password.
 */
public class InitialPasswordChangeRequest {

    @Email(message = "Username needs to be an email")
    @NotBlank(message = "username is required")
    private String username;

    public InitialPasswordChangeRequest() { }

    public InitialPasswordChangeRequest(String userName) {
        this.username = userName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
