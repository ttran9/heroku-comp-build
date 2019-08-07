package tran.compbuildbackend.domain.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import tran.compbuildbackend.domain.computerbuild.ComputerBuild;
import tran.compbuildbackend.domain.security.ChangePasswordToken;
import tran.compbuildbackend.domain.security.EmailVerificationToken;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import static tran.compbuildbackend.constants.fields.FieldValueConstants.FULL_NAME_MISSING_ERROR;

@Entity
@Table(name = "User")
public class ApplicationUser implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email
    @Column(unique = true)
    @NotBlank(message = "email is required")
    private String email;
    @NotBlank(message = FULL_NAME_MISSING_ERROR)
    private String fullName;
    @NotBlank(message = "Password field is required")
    private String password;
    @Transient // don't want to store the confirmPW.
    private String confirmPassword;
    @NotBlank(message = "username is required")
    @Column(unique = true)
    private String username;

    private LocalDate createdAt;
    private LocalDate updatedAt;
    private boolean enabled;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH, mappedBy = "user", orphanRemoval = true)
    private EmailVerificationToken emailVerificationToken;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH, mappedBy = "user", orphanRemoval = true)
    private ChangePasswordToken changePasswordToken;

    @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER, orphanRemoval = true, mappedBy = "user")
    private List<ComputerBuild> computerBuild = new LinkedList<>();

    public ApplicationUser() { }

    public ApplicationUser(@NotBlank(message = "username is required") String username,
                           @NotBlank(message = "Password field is required") String password, String confirmPassword) {
        this.username = username;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }

    public ApplicationUser(@NotBlank(message = "username is required") String username,
                           @Email @NotBlank(message = "email is required") String email,
                           @NotBlank(message = "Password field is required") String password, String confirmPassword) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }

    public ApplicationUser(@NotBlank(message = "username is required") String username,
                           @Email @NotBlank(message = "email is required") String email,
                           @NotBlank(message = FULL_NAME_MISSING_ERROR) String fullName,
                           @NotBlank(message = "Password field is required") String password, String confirmPassword) {
        this.username = username;
        this.email = email;
        this.fullName = fullName;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }



    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDate.now();
    }

    @PostPersist
    protected void onUpdate() {
        this.updatedAt = LocalDate.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
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

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDate getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<ComputerBuild> getComputerBuild() {
        return computerBuild;
    }

    public void setComputerBuild(List<ComputerBuild> computerBuild) {
        this.computerBuild = computerBuild;
    }

    public EmailVerificationToken getEmailVerificationToken() {
        return emailVerificationToken;
    }

    public void setEmailVerificationToken(EmailVerificationToken emailVerificationToken) {
        this.emailVerificationToken = emailVerificationToken;
    }

    public ChangePasswordToken getChangePasswordToken() {
        return changePasswordToken;
    }

    public void setChangePasswordToken(ChangePasswordToken changePasswordToken) {
        this.changePasswordToken = changePasswordToken;
    }

    /*
     * UserDetails interface methods
     */
    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // no roles in our app so null is ok here.
        return null;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
