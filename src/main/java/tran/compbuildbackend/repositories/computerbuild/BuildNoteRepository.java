package tran.compbuildbackend.repositories.computerbuild;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tran.compbuildbackend.domain.computerbuild.BuildNote;

@Repository
public interface BuildNoteRepository extends CrudRepository<BuildNote, Long> {
    BuildNote getBuildNoteByUniqueIdentifier(String uniqueIdentifier);
}
