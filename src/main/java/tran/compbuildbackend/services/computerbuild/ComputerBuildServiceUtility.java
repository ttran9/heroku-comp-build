package tran.compbuildbackend.services.computerbuild;

import tran.compbuildbackend.domain.computerbuild.AbstractNote;
import tran.compbuildbackend.domain.computerbuild.ComputerBuild;
import tran.compbuildbackend.domain.user.ApplicationUser;
import tran.compbuildbackend.repositories.computerbuild.ComputerBuildRepository;
import tran.compbuildbackend.services.security.utility.SecurityUtil;

import static tran.compbuildbackend.constants.computerbuild.ComputerBuildConstants.*;
import static tran.compbuildbackend.constants.exception.ExceptionConstants.INVALID_IDENTIFIER_FORMAT;
import static tran.compbuildbackend.exceptions.ExceptionUtility.throwMessageException;

public class ComputerBuildServiceUtility {

    public static ComputerBuild getComputerBuildByBuildId(ComputerBuildRepository computerBuildRepository, String buildIdentifier) {
        ComputerBuild computerBuild = computerBuildRepository.getComputerBuildByBuildIdentifier(buildIdentifier);
        if(computerBuild == null) {
            throwMessageException(COMPUTER_BUILD_DOES_NOT_EXIST);
        }
        if(computerBuild.getId() == null || computerBuild.getUser() == null) {
            throwMessageException(COMPUTER_BUILD_DOES_NOT_EXIST);
        }
        return computerBuild;
    }

    public static ComputerBuild verifyOwnerOfComputerBuild(ComputerBuildRepository computerBuildRepository, String buildIdentifier) {
        ApplicationUser user = SecurityUtil.getLoggedInUser();

        ComputerBuild oldBuild = getComputerBuildByBuildId(computerBuildRepository, buildIdentifier);

        if(oldBuild.getUser().getUsername().equals(user.getUsername())) {
            return oldBuild;
        }
        throwMessageException(COMPUTER_BUILD_CANNOT_BE_MODIFIED);
        return null; // this won't be hit because an exception will be thrown.
    }

    /**
     * @param prefix The prefix based off the computer detail this string is a part of ('dir' for direction,
     *               'ocn' for overclockingnote, 'pur' for purpose, 'bn' for buildnote, or 'cp' for computer part).
     * @param computerBuild The computer build object to be able check the number of directions or overclocking notes, etc.
     * @return The unique identifier for a computer build detail.
     */
    public static String generateComputerBuildDetail(String prefix, ComputerBuild computerBuild) {
        StringBuilder uniqueIdentifier = new StringBuilder();
        String buildIdentifier = computerBuild.getBuildIdentifier();
        uniqueIdentifier.append(buildIdentifier);
        uniqueIdentifier.append(DETAIL_IDENTIFIER_SEPARATOR);
        switch(prefix) {
            case DIRECTION_ABBREVIATION:
                int newDirectionsCount = computerBuild.getDirectionsCount() + 1;
                uniqueIdentifier.append(DIRECTION_ABBREVIATION);
                uniqueIdentifier.append(DETAIL_IDENTIFIER_SEPARATOR);
                uniqueIdentifier.append(newDirectionsCount);
                computerBuild.setDirectionsCount(newDirectionsCount);
                break;
            case COMPUTER_PART_ABBREVIATION:
                int newComputerPartsCount = computerBuild.getComputerPartsCount() + 1;
                uniqueIdentifier.append(COMPUTER_PART_ABBREVIATION);
                uniqueIdentifier.append(DETAIL_IDENTIFIER_SEPARATOR);
                uniqueIdentifier.append(newComputerPartsCount);
                computerBuild.setComputerPartsCount(newComputerPartsCount);
                break;
            case PURPOSE_ABBREVIATION:
                int newPurposeCount = computerBuild.getPurposeCount() + 1;
                uniqueIdentifier.append(PURPOSE_ABBREVIATION);
                uniqueIdentifier.append(DETAIL_IDENTIFIER_SEPARATOR);
                uniqueIdentifier.append(newPurposeCount);
                computerBuild.setPurposeCount(newPurposeCount);
                break;
            case OVERCLOCKING_NOTE_ABBREVIATION:
                int newOverclockingNotesCount = computerBuild.getOverclockingNotesCount() + 1;
                uniqueIdentifier.append(OVERCLOCKING_NOTE_ABBREVIATION);
                uniqueIdentifier.append(DETAIL_IDENTIFIER_SEPARATOR);
                uniqueIdentifier.append(newOverclockingNotesCount);
                computerBuild.setOverclockingNotesCount(newOverclockingNotesCount);
                break;
            case BUILD_NOTE_ABBREVIATION:
                int newBuildNotesCount = computerBuild.getBuildNotesCount() + 1;
                uniqueIdentifier.append(BUILD_NOTE_ABBREVIATION);
                uniqueIdentifier.append(DETAIL_IDENTIFIER_SEPARATOR);
                uniqueIdentifier.append(newBuildNotesCount);
                computerBuild.setBuildNotesCount(newBuildNotesCount);
                break;
            default:
                break;
        }
        return uniqueIdentifier.toString();
    }

    public static void setAbstractNote(ComputerBuild computerBuild, AbstractNote note, String noteAbbreviation) {
        if (note.getPriority() < LOWEST_PRIORITY || note.getPriority() > HIGHEST_PRIORITY) {
            note.setPriority(DEFAULT_PRIORITY);
        }
        note.setUniqueIdentifier(generateComputerBuildDetail(noteAbbreviation, computerBuild));
        note.setComputerBuild(computerBuild);
    }

    public static ComputerBuild verifyComputerDetailOwner(String uniqueIdentifier, ComputerBuildRepository computerBuildRepository) {
        String[] buildIdentifier = uniqueIdentifier.split("-");

        if(buildIdentifier.length != 3) {
            throwMessageException(INVALID_IDENTIFIER_FORMAT);
        }

        return verifyOwnerOfComputerBuild(computerBuildRepository, buildIdentifier[0]);
    }

}
