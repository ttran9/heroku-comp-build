package tran.compbuildbackend.domain.security;


import tran.compbuildbackend.domain.user.ApplicationUser;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@MappedSuperclass
public abstract class VerificationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(name="token")
    @NotBlank
    protected String token;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    protected ApplicationUser user;

    @Column(name="created_date")
    protected LocalDateTime createdDate;

    @Column(name="expiration_date")
    protected LocalDateTime expirationDate;

    VerificationToken() { }

    VerificationToken(String token, ApplicationUser user) {
        this.token = token;
        this.user = user;
        this.createdDate = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public ApplicationUser getUser() {
        return user;
    }

    public void setUser(ApplicationUser user) {
        this.user = user;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDateTime expirationDate) {
        this.expirationDate = expirationDate;
    }

    public LocalDateTime calculateExpirationDate(int expirationTimeInMinutes) {
        /*
         * set an expiration date (this will be used by the change password token and the email verification token)
         * so a parameter is needed instead of setting a field and using that value because in that case both tokens
         * would have the same expiration time.
         */
        return this.createdDate.plusMinutes(expirationTimeInMinutes);
    }
}
