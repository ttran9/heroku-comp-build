package tran.compbuildbackend.services.computerbuild;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tran.compbuildbackend.domain.computerbuild.ComputerBuild;
import tran.compbuildbackend.domain.computerbuild.OverclockingNote;
import tran.compbuildbackend.repositories.computerbuild.ComputerBuildRepository;
import tran.compbuildbackend.repositories.computerbuild.OverclockingNoteRepository;

import static tran.compbuildbackend.constants.computerbuild.ComputerBuildConstants.*;
import static tran.compbuildbackend.exceptions.ExceptionUtility.throwMessageException;

@Service
public class OverclockingNoteServiceImpl implements OverclockingNoteService {

    private OverclockingNoteRepository overclockingNoteRepository;

    private ComputerBuildRepository computerBuildRepository;

    public OverclockingNoteServiceImpl(OverclockingNoteRepository overclockingNoteRepository, ComputerBuildRepository computerBuildRepository) {
        this.overclockingNoteRepository = overclockingNoteRepository;
        this.computerBuildRepository = computerBuildRepository;
    }

    @Transactional
    @Override
    public OverclockingNote create(String buildIdentifier, OverclockingNote overclockingNote) {
        // ensure one is the owner of the computer build that the overclocking note is being added to.
        ComputerBuild retrievedComputerBuild = ComputerBuildServiceUtility.verifyOwnerOfComputerBuild(
                computerBuildRepository, buildIdentifier);

        ComputerBuildServiceUtility.setAbstractNote(retrievedComputerBuild, overclockingNote, OVERCLOCKING_NOTE_ABBREVIATION);

        return overclockingNoteRepository.save(overclockingNote);
    }

    @Override
    public OverclockingNote update(OverclockingNote newOverclockingNote, String uniqueIdentifier) {
        // verify that the owner is modifying the overclocking note and that it is a properly formatted unique identifier.
        ComputerBuildServiceUtility.verifyComputerDetailOwner(uniqueIdentifier, computerBuildRepository);

        getOverclockingNote(uniqueIdentifier, OVERCLOCKING_NOTE_CANNOT_BE_UPDATED);

        return overclockingNoteRepository.save(newOverclockingNote);
    }

    @Override
    public void delete(String uniqueIdentifier) {
        // verify that the user owns/created the overclocking note before deleting and that it is a properly formatted unique identifier.
        ComputerBuildServiceUtility.verifyComputerDetailOwner(uniqueIdentifier, computerBuildRepository);

        // verify if the unique identifier points to the object to be updated.
        OverclockingNote overclockingNote = getOverclockingNote(uniqueIdentifier, OVERCLOCKING_NOTE_CANNOT_BE_DELETED);

        overclockingNoteRepository.delete(overclockingNote);
    }

    @Override
    public OverclockingNote getFromUniqueIdentifier(String uniqueIdentifier) {
        return getOverclockingNote(uniqueIdentifier, INVALID_OVERCLOCKING_NOTE);
    }

    private OverclockingNote getOverclockingNote(String uniqueIdentifier, String exceptionMessage) {
        OverclockingNote overclockingNote = overclockingNoteRepository.getOverclockingNoteByUniqueIdentifier(uniqueIdentifier);

        // make sure you are passing in a valid unique identifier.
        if(overclockingNote == null) {
            throwMessageException(exceptionMessage);
        }
        return overclockingNote;
    }
}
