package tran.compbuildbackend.repositories.computerbuild;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tran.compbuildbackend.domain.computerbuild.ComputerBuild;
import tran.compbuildbackend.domain.user.ApplicationUser;

@Repository
public interface ComputerBuildRepository extends CrudRepository<ComputerBuild, Long> {

    ComputerBuild getComputerBuildByBuildIdentifier(String buildIdentifier);
    Iterable<ComputerBuild> getAllByUser(ApplicationUser user);
}
