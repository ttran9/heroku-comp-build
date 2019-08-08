package tran.compbuildbackend.services.computerbuild;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tran.compbuildbackend.domain.computerbuild.BuildNote;
import tran.compbuildbackend.domain.computerbuild.ComputerBuild;
import tran.compbuildbackend.repositories.computerbuild.BuildNoteRepository;
import tran.compbuildbackend.repositories.computerbuild.ComputerBuildRepository;

import static tran.compbuildbackend.constants.computerbuild.ComputerBuildConstants.*;
import static tran.compbuildbackend.exceptions.ExceptionUtility.throwMessageException;

@Service
public class BuildNoteServiceImpl implements BuildNoteService {

    private BuildNoteRepository buildNoteRepository;

    private ComputerBuildRepository computerBuildRepository;

    public BuildNoteServiceImpl(BuildNoteRepository buildNoteRepository, ComputerBuildRepository computerBuildRepository) {
        this.buildNoteRepository = buildNoteRepository;
        this.computerBuildRepository = computerBuildRepository;
    }

    @Transactional
    @Override
    public BuildNote create(String buildIdentifier, BuildNote buildNote) {
        // ensure one is the owner of the computer build that the build note is being added to.
        ComputerBuild retrievedComputerBuild = ComputerBuildServiceUtility.verifyOwnerOfComputerBuild(
                computerBuildRepository, buildIdentifier);

        ComputerBuildServiceUtility.setAbstractNote(retrievedComputerBuild, buildNote, BUILD_NOTE_ABBREVIATION);

        return buildNoteRepository.save(buildNote);
    }

    @Override
    public BuildNote update(BuildNote newBuildNote, String noteUniqueIdentifier) {

        // verify that the owner is modifying the build note and that it is a properly formatted unique identifier.
        ComputerBuildServiceUtility.verifyComputerDetailOwner(noteUniqueIdentifier, computerBuildRepository);

        getBuildNote(noteUniqueIdentifier, BUILD_NOTE_CANNOT_BE_UPDATED);

        return buildNoteRepository.save(newBuildNote);
    }

    @Override
    public BuildNote delete(String noteUniqueIdentifier) {
        // verify that the user owns/created the build note before deleting and that it is a properly formatted unique identifier.
        ComputerBuildServiceUtility.verifyComputerDetailOwner(noteUniqueIdentifier, computerBuildRepository);

        // verify if the unique identifier points to the object to be updated.
        BuildNote buildNote = getBuildNote(noteUniqueIdentifier, BUILD_NOTE_CANNOT_BE_DELETED);

        buildNoteRepository.delete(buildNote);

        return buildNote;
    }

    @Override
    public BuildNote getFromUniqueIdentifier(String noteUniqueIdentifier) {
        return getBuildNote(noteUniqueIdentifier, INVALID_BUILD_NOTE);
    }

    private BuildNote getBuildNote(String noteUniqueIdentifier, String exceptionMessage) {
        BuildNote buildNote = buildNoteRepository.getBuildNoteByUniqueIdentifier(noteUniqueIdentifier);

        // make sure you are passing in a valid unique identifier.
        if(buildNote == null) {
            throwMessageException(exceptionMessage);
        }
        return buildNote;
    }
}
