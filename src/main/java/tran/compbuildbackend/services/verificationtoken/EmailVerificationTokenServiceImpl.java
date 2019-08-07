package tran.compbuildbackend.services.verificationtoken;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tran.compbuildbackend.domain.security.EmailVerificationToken;
import tran.compbuildbackend.domain.user.ApplicationUser;
import tran.compbuildbackend.repositories.security.EmailVerificationTokenRepository;

import java.util.UUID;

import static tran.compbuildbackend.constants.exception.ExceptionConstants.TOKEN_IS_NOT_PRESENT;
import static tran.compbuildbackend.exceptions.ExceptionUtility.throwTokenException;

@Service
public class EmailVerificationTokenServiceImpl implements VerificationTokenService {

    private EmailVerificationTokenRepository emailVerificationTokenRepository;

    @Autowired
    public EmailVerificationTokenServiceImpl(EmailVerificationTokenRepository emailVerificationTokenRepository) {
        this.emailVerificationTokenRepository = emailVerificationTokenRepository;
    }

    @Override
    public String createVerificationToken(ApplicationUser user) {
        String token = UUID.randomUUID().toString();
        EmailVerificationToken newUserToken = new EmailVerificationToken(token, user);
        return VerificationTokenUtil.createToken(user, token, emailVerificationTokenRepository, newUserToken);
    }

    @Override
    public EmailVerificationToken getVerificationToken(String verificationToken) {
        return emailVerificationTokenRepository.findByToken(verificationToken);
    }

    @Override
    public ApplicationUser validateVerificationToken(String token) {
        EmailVerificationToken emailVerificationToken = getVerificationToken(token);
        if(emailVerificationToken == null) {
            throwTokenException(TOKEN_IS_NOT_PRESENT);
        }
        /*
         * for now the email verification token is used to help the user activate their account so don't check if it
         */
//        SecurityUtil.isTokenExpired(emailVerificationToken, EMAIL_VERIFICATION_TOKEN_TYPE);
        return emailVerificationToken.getUser();
    }
}
