package tran.compbuildbackend.services.computerbuild;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tran.compbuildbackend.domain.computerbuild.ComputerBuild;
import tran.compbuildbackend.domain.computerbuild.ComputerPart;
import tran.compbuildbackend.repositories.computerbuild.ComputerBuildRepository;
import tran.compbuildbackend.repositories.computerbuild.ComputerPartRepository;

import static tran.compbuildbackend.constants.computerbuild.ComputerBuildConstants.*;
import static tran.compbuildbackend.exceptions.ExceptionUtility.throwMessageException;

@Service
public class ComputerPartServiceImpl implements ComputerPartService {

    private ComputerPartRepository computerPartRepository;

    private ComputerBuildRepository computerBuildRepository;

    public ComputerPartServiceImpl(ComputerPartRepository computerPartRepository, ComputerBuildRepository computerBuildRepository) {
        this.computerPartRepository = computerPartRepository;
        this.computerBuildRepository = computerBuildRepository;
    }

    @Transactional
    @Override
    public ComputerPart create(String buildIdentifier, ComputerPart computerPart) {
        ComputerBuild retrievedComputerBuild = ComputerBuildServiceUtility.verifyOwnerOfComputerBuild(
                computerBuildRepository, buildIdentifier);

        computerPart.setUniqueIdentifier(ComputerBuildServiceUtility.generateComputerBuildDetail(
                COMPUTER_PART_ABBREVIATION, retrievedComputerBuild));
        computerPart.setComputerBuild(retrievedComputerBuild);

        return computerPartRepository.save(computerPart);
    }


    @Override
    public ComputerPart update(ComputerPart newComputerPart, String uniqueIdentifier) {
        // verify that the owner is modifying the computer part and that it is a properly formatted unique identifier.
        ComputerBuildServiceUtility.verifyComputerDetailOwner(uniqueIdentifier, computerBuildRepository);

        getComputerPart(uniqueIdentifier, COMPUTER_PART_CANNOT_BE_UPDATED);

        return computerPartRepository.save(newComputerPart);
    }

    @Override
    public void delete(String uniqueIdentifier) {
        // verify that the user owns/created the computer part before deleting and that it is a properly formatted unique identifier.
        ComputerBuildServiceUtility.verifyComputerDetailOwner(uniqueIdentifier, computerBuildRepository);

        // verify if the unique identifier points to the object to be updated.
        ComputerPart computerPart = getComputerPart(uniqueIdentifier, COMPUTER_PART_CANNOT_BE_DELETED);

        computerPartRepository.delete(computerPart);
    }

    @Override
    public ComputerPart getFromUniqueIdentifier(String uniqueIdentifier) {
        return getComputerPart(uniqueIdentifier, INVALID_COMPUTER_PART);
    }

    private ComputerPart getComputerPart(String uniqueIdentifier, String exceptionMessage) {
        ComputerPart computerPart = computerPartRepository.getComputerPartByUniqueIdentifier(uniqueIdentifier);

        // make sure you are passing in a valid unique identifier.
        if(computerPart == null) {
            throwMessageException(exceptionMessage);
        }
        return computerPart;
    }
}
