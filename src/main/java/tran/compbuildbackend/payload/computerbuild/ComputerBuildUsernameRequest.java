package tran.compbuildbackend.payload.computerbuild;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

/**
 * An object that holds the username
 */
public class ComputerBuildUsernameRequest {

    /**
     * The name of the user (an email address).
     */
    @Email
    @NotNull
    private String userName;

    public ComputerBuildUsernameRequest() { }

    public ComputerBuildUsernameRequest(@Email @NotNull String userName) {
        this.userName = userName;
    }
}
