package tran.compbuildbackend.dto.user;

import tran.compbuildbackend.domain.security.VerificationToken;

import javax.validation.constraints.NotBlank;

import static tran.compbuildbackend.constants.fields.FieldValueConstants.FULL_NAME_MISSING_ERROR;

/**
 * I do not include fields createdAt and updatedAt because for this application there is no use in knowing when a user has
 * signed up or last posted. We will only care when a "ComputerBuild" object was created.
 */
public class ApplicationUserDto {

    @NotBlank(message = "username is required")
    private String username;
    @NotBlank(message = FULL_NAME_MISSING_ERROR)
    private String fullName;

    private VerificationToken verificationToken;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public VerificationToken getVerificationToken() {
        return verificationToken;
    }

    public void setVerificationToken(VerificationToken verificationToken) {
        this.verificationToken = verificationToken;
    }

    public ApplicationUserDto(@NotBlank(message = "username is required") String username, VerificationToken verificationToken) {
        this.username = username;
        this.verificationToken = verificationToken;
    }
}
