package tran.compbuildbackend.services.computerbuild;

import tran.compbuildbackend.domain.computerbuild.ComputerBuild;

public interface ComputerBuildService {

    /**
     * Adds a new computer build object in the database.
     * @param computerBuild The computer build object to be added.
     * @return Returns the persisted computer build object.
     */
    ComputerBuild createNewComputerBuild(ComputerBuild computerBuild);

    /**
     * removes the computer build with the specified buildIdentifier.
     * @param buildIdentifier The unique identifier of the computer build to be deleted.
     */
    void deleteComputerBuild(String buildIdentifier);

    /**
     * @param buildIdentifier The unique identifier of the ComputerBuild to be retrieved.
     * @return The ComputerBuild with the associated build identifier or a custom exception is thrown if the ComputerBuild
     * cannot be found.
     */
    ComputerBuild getComputerBuildByBuildIdentifier(String buildIdentifier);

    /**
     * @return A list of all the computer builds.
     */
    Iterable<ComputerBuild> getAllComputerBuilds();

    /**
     * @param userName The name of the user to retrieve the computer builds of.
     * @return A list of all the computer builds by the specified user.
     */
    Iterable<ComputerBuild> getAllComputerBuildsFromUser(String userName);
}
