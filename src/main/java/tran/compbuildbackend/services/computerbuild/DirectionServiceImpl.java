package tran.compbuildbackend.services.computerbuild;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tran.compbuildbackend.domain.computerbuild.ComputerBuild;
import tran.compbuildbackend.domain.computerbuild.Direction;
import tran.compbuildbackend.repositories.computerbuild.ComputerBuildRepository;
import tran.compbuildbackend.repositories.computerbuild.DirectionRepository;

import static tran.compbuildbackend.constants.computerbuild.ComputerBuildConstants.*;
import static tran.compbuildbackend.exceptions.ExceptionUtility.throwMessageException;

@Service
public class DirectionServiceImpl implements DirectionService {

    private DirectionRepository directionRepository;

    private ComputerBuildRepository computerBuildRepository;

    public DirectionServiceImpl(DirectionRepository directionRepository, ComputerBuildRepository computerBuildRepository) {
        this.directionRepository = directionRepository;
        this.computerBuildRepository = computerBuildRepository;
    }

    @Transactional
    @Override
    public Direction create(String buildIdentifier, Direction direction) {
        // ensure one is the owner of the computer build that the direction is being added to.
        ComputerBuild retrievedComputerBuild = ComputerBuildServiceUtility.verifyOwnerOfComputerBuild(
                computerBuildRepository, buildIdentifier);

        direction.setUniqueIdentifier(ComputerBuildServiceUtility.generateComputerBuildDetail(
                DIRECTION_ABBREVIATION, retrievedComputerBuild));
        direction.setComputerBuild(retrievedComputerBuild);

        return directionRepository.save(direction);
    }

    @Override
    public Direction update(Direction newDirection, String uniqueIdentifier) {
        // verify that the owner is modifying the direction note and that it is a properly formatted unique identifier.
        ComputerBuildServiceUtility.verifyComputerDetailOwner(uniqueIdentifier, computerBuildRepository);

        getDirection(uniqueIdentifier, DIRECTION_CANNOT_BE_UPDATED);

        return directionRepository.save(newDirection);
    }

    @Override
    public Direction delete(String uniqueIdentifier) {
        // verify that the user owns/created is modifying the direction before deleting and that it is a properly formatted unique identifier.
        ComputerBuildServiceUtility.verifyComputerDetailOwner(uniqueIdentifier, computerBuildRepository);

        // verify if the unique identifier points to the object to be updated.
        Direction direction = getDirection(uniqueIdentifier, DIRECTION_CANNOT_BE_DELETED);

        directionRepository.delete(direction);

        return direction;
    }

    @Override
    public Direction getFromUniqueIdentifier(String uniqueIdentifier) {
        return getDirection(uniqueIdentifier, INVALID_DIRECTION);
    }

    private Direction getDirection(String uniqueIdentifier, String exceptionMessage) {
        Direction direction = directionRepository.getDirectionByUniqueIdentifier(uniqueIdentifier);

        // make sure you are passing in a valid unique identifier.
        if(direction == null) {
            throwMessageException(exceptionMessage);
        }
        return direction;
    }
}
