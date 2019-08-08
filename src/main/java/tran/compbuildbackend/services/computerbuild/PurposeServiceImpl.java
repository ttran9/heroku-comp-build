package tran.compbuildbackend.services.computerbuild;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tran.compbuildbackend.domain.computerbuild.ComputerBuild;
import tran.compbuildbackend.domain.computerbuild.Purpose;
import tran.compbuildbackend.repositories.computerbuild.ComputerBuildRepository;
import tran.compbuildbackend.repositories.computerbuild.PurposeRepository;

import static tran.compbuildbackend.constants.computerbuild.ComputerBuildConstants.*;
import static tran.compbuildbackend.exceptions.ExceptionUtility.throwMessageException;

@Service
public class PurposeServiceImpl implements PurposeService {

    private PurposeRepository purposeRepository;

    private ComputerBuildRepository computerBuildRepository;

    public PurposeServiceImpl(PurposeRepository purposeRepository, ComputerBuildRepository computerBuildRepository) {
        this.purposeRepository = purposeRepository;
        this.computerBuildRepository = computerBuildRepository;
    }

    @Transactional
    @Override
    public Purpose create(String buildIdentifier, Purpose purpose) {
        // ensure one is the owner of the computer build that the purpose is being added to.
        ComputerBuild retrievedComputerBuild = ComputerBuildServiceUtility.verifyOwnerOfComputerBuild(
                computerBuildRepository, buildIdentifier);

        ComputerBuildServiceUtility.setAbstractNote(retrievedComputerBuild, purpose, PURPOSE_ABBREVIATION);

        return purposeRepository.save(purpose);
    }

    @Override
    public Purpose update(Purpose newPurpose, String uniqueIdentifier) {
        // verify that the owner is modifying the direction note and that it is a properly formatted unique identifier
        ComputerBuildServiceUtility.verifyComputerDetailOwner(uniqueIdentifier, computerBuildRepository);

        // check the formatting of the unique identifier.
        getPurpose(uniqueIdentifier, PURPOSE_CANNOT_BE_UPDATED);

        return purposeRepository.save(newPurpose);
    }

    @Override
    public Purpose delete(String uniqueIdentifier) {
        // verify that the user owns/created the direction before deleting and that it is a properly formatted unique identifier.
        ComputerBuildServiceUtility.verifyComputerDetailOwner(uniqueIdentifier, computerBuildRepository);

        // verify if the unique identifier points to the object to be updated.
        Purpose purpose = getPurpose(uniqueIdentifier, PURPOSE_CANNOT_BE_DELETED);

        purposeRepository.delete(purpose);

        return purpose;
    }

    @Override
    public Purpose getFromUniqueIdentifier(String uniqueIdentifier) {
        return getPurpose(uniqueIdentifier, INVALID_PURPOSE);
    }

    private Purpose getPurpose(String uniqueIdentifier, String exceptionMessage) {
        Purpose purpose = purposeRepository.getPurposeByUniqueIdentifier(uniqueIdentifier);

        // make sure you are passing in a valid unique identifier.
        if(purpose == null) {
            throwMessageException(exceptionMessage);
        }
        return purpose;
    }
}
