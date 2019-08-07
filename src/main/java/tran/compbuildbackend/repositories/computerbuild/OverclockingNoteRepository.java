package tran.compbuildbackend.repositories.computerbuild;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tran.compbuildbackend.domain.computerbuild.OverclockingNote;

@Repository
public interface OverclockingNoteRepository extends CrudRepository<OverclockingNote, Long> {
    OverclockingNote getOverclockingNoteByUniqueIdentifier(String uniqueIdentifier);
}
