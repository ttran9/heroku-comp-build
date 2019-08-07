package tran.compbuildbackend.repositories.computerbuild;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tran.compbuildbackend.domain.computerbuild.ComputerPart;

@Repository
public interface ComputerPartRepository extends CrudRepository<ComputerPart, Long> {
    ComputerPart getComputerPartByUniqueIdentifier(String uniqueIdentifier);
}
