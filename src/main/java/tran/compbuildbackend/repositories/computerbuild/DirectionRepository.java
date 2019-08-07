package tran.compbuildbackend.repositories.computerbuild;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tran.compbuildbackend.domain.computerbuild.Direction;

@Repository
public interface DirectionRepository extends CrudRepository<Direction, Long> {
    Direction getDirectionByUniqueIdentifier(String uniqueIdentifier);
}
