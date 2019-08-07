package tran.compbuildbackend.services.verificationtoken;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.repository.CrudRepository;
import tran.compbuildbackend.domain.security.VerificationToken;
import tran.compbuildbackend.domain.user.ApplicationUser;

import static tran.compbuildbackend.constants.exception.ExceptionConstants.CANNOT_FIND_USER;
import static tran.compbuildbackend.exceptions.ExceptionUtility.throwMessageException;


public class VerificationTokenUtil {

    public static String createToken(ApplicationUser user, String token, CrudRepository crudRepository, VerificationToken verificationToken) {
        if(user == null) {
            return null;
        }
        if(user.getId() == null) {
            return null;
        }
        try {
            crudRepository.save(verificationToken);
        } catch(DataIntegrityViolationException ex) {
            throwMessageException(CANNOT_FIND_USER);
        } catch(Exception ex) {
            return null;
        }
        return token;
    }
}
