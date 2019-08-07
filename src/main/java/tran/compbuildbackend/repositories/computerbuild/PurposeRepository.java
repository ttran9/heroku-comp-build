package tran.compbuildbackend.repositories.computerbuild;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tran.compbuildbackend.domain.computerbuild.Purpose;

@Repository
public interface PurposeRepository extends CrudRepository<Purpose, Long> {
    Purpose getPurposeByUniqueIdentifier(String uniqueIdentifier);
}
