package tran.compbuildbackend.services.computerbuild;

public interface ComputerBuildDetailService<T> {
    /**
     * Adds an object to the database.
     * @param buildIdentifier The identifier of the computer build to be associated with the object.
     * @param detailObject The detail object (overclocking/build note, purpose, direction, or computer part) to be persisted.
     * @return Returns the detail object linked/associated with the ComputerBuild.
     */
    T create(String buildIdentifier, T detailObject);

    /**
     * Update the note with the given identifier.
     * @param newDetailObject The new detail object with the updated information.
     * @param uniqueIdentifier The unique identifier for the note to be updated.
     * @return Returns the updated note.
     */
    T update(T newDetailObject, String uniqueIdentifier);

    /**
     * Deletes the detail object with the given identifier.
     * @param uniqueIdentifier The unique identifier for the detail object to be deleted.
     */
    void delete(String uniqueIdentifier);

    /**
     * Gets the detail object with the unique identifier.
     * @param uniqueIdentifier The unique identifier to find the detail object.
     * @return Returns the detail object with the identifier or throws an exception of the identifier is invalid.
     */
    T getFromUniqueIdentifier(String uniqueIdentifier);
}
