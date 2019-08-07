package tran.compbuildbackend.domain.security;

import tran.compbuildbackend.domain.user.ApplicationUser;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "EmailVerificationToken")
public class EmailVerificationToken extends VerificationToken {
    @Transient
    private int expirationInMinutes = 30;

    public EmailVerificationToken() {
        super();
    }

    public EmailVerificationToken(String token, ApplicationUser user) {
        super(token, user);
//        this.expirationDate = super.calculateExpirationDate(expirationInMinutes);
    }
}
