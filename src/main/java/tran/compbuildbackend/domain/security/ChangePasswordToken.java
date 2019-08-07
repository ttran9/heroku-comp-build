package tran.compbuildbackend.domain.security;

import tran.compbuildbackend.domain.user.ApplicationUser;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "ChangePasswordToken")
public class ChangePasswordToken extends VerificationToken {
    @Transient
    private int expirationInMinutes = 15;

    public ChangePasswordToken() { super(); }

    public ChangePasswordToken(String token, ApplicationUser user) {
        super(token, user);
        this.expirationDate = super.calculateExpirationDate(expirationInMinutes);
    }


}
