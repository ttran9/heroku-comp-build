package tran.compbuildbackend.exceptions.security;

import static tran.compbuildbackend.constants.fields.FieldValueConstants.INVALID_PASSWORD_ERROR;
import static tran.compbuildbackend.constants.fields.FieldValueConstants.INVALID_USERNAME_ERROR;

/**
 * A custom JSON object that will be returned when we are trying to access a resource that requires authentication.
 */
public class InvalidLoginExceptionResponse {
    private String username;
    private String password;

    public InvalidLoginExceptionResponse() {
        this.username = INVALID_USERNAME_ERROR;
        this.password = INVALID_PASSWORD_ERROR;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
