package tran.compbuildbackend.services.users;


import tran.compbuildbackend.domain.user.ApplicationUser;

import javax.servlet.http.HttpServletRequest;

public interface ApplicationUserService {
    /**
     * creates the user attempting to be registered.
     * @param newApplicationUser The user to be registered.
     * @param request Object containing information about from the HTTP request (such as URL information).
     * @return Returns the new user with fields entered.
     */
    ApplicationUser persistUser(ApplicationUser newApplicationUser, HttpServletRequest request);

    /**
     * Delete the user if there is some kind of error sending the email with the token required for user registration.
     * @param newApplicationUser The user to be deleted.
     */
    void removeUser(ApplicationUser newApplicationUser);

    /**
     * Save (an existing registered user) with an updated "enabled" field set to true.
     * @param user The registered user to be updated.
     */
    void enableRegisteredUser(ApplicationUser user);

    /**
     * Sends an email with the token required to activate the registered user's account.
     * @param registeredUser The object containing the registered user name (email).
     * @param request The request containing information about the app's URL.
     */
    void sendSuccessRegistrationEmail(ApplicationUser registeredUser, HttpServletRequest request);

    /**
     * Takes in a new password and a user to change the password for.
     * @param newPassword The new password the user is changing to.
     * @param user The user to change the password for.
     */
    void changeUserPassword(String newPassword, ApplicationUser user);

    /**
     * Sends a URL to change the user's account along with a token which is necessary to change the password.
     * @param userName The email account that the change password link will be emailed to
     * @param request The request containing information about the app's URL.
     */
    ApplicationUser sendPasswordChangeEmail(String userName, HttpServletRequest request);

    /**
     * Method used to enable the user without going through the registration process. This is meant to be used in a test/development
     * environment only.
     * @param user The user (with a username field) to be updated.
     */
    void enableUser(ApplicationUser user);

    /**
     * @param email The email address of the user to be returned.
     * @param exceptionType A value used to determine what type of exception is thrown when the user cannot be found.
     * @return The user with the associated email if found, if not a custom exception is thrown.
     */
    ApplicationUser getUserByEmail(String email, int exceptionType);

    /**
     * @param userName The user name of the user to be returned.
     * @return The user with the associated user name if found, if not a custom exception is thrown.
     */
    ApplicationUser getUserByUsername(String userName);
}
