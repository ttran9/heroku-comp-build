package tran.compbuildbackend.repositories.security;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tran.compbuildbackend.domain.security.ChangePasswordToken;
import tran.compbuildbackend.domain.user.ApplicationUser;

@Repository
public interface ChangePasswordTokenRepository extends CrudRepository<ChangePasswordToken, Long> {

    ChangePasswordToken findByToken(String token);
    ChangePasswordToken findByUser(ApplicationUser user);
    void deleteByToken(String token);
}
