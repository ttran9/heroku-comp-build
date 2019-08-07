package tran.compbuildbackend.repositories.security;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tran.compbuildbackend.domain.security.EmailVerificationToken;
import tran.compbuildbackend.domain.user.ApplicationUser;

@Repository
public interface EmailVerificationTokenRepository extends CrudRepository<EmailVerificationToken, Long> {

    EmailVerificationToken findByToken(String token);
    EmailVerificationToken findByUser(ApplicationUser user);
    void deleteByToken(String token);
}
