package tran.compbuildbackend.services.users;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tran.compbuildbackend.domain.security.ChangePasswordToken;
import tran.compbuildbackend.domain.security.EmailVerificationToken;
import tran.compbuildbackend.domain.user.ApplicationUser;
import tran.compbuildbackend.event.OnPasswordResetRequestEvent;
import tran.compbuildbackend.event.OnRegistrationSuccessEvent;
import tran.compbuildbackend.exceptions.request.GenericRequestException;
import tran.compbuildbackend.exceptions.request.MultipleFieldsException;
import tran.compbuildbackend.repositories.security.ChangePasswordTokenRepository;
import tran.compbuildbackend.repositories.security.EmailVerificationTokenRepository;
import tran.compbuildbackend.repositories.users.ApplicationUserRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

import static tran.compbuildbackend.constants.exception.ExceptionConstants.*;
import static tran.compbuildbackend.constants.fields.FieldConstants.EMAIL_FIELD;
import static tran.compbuildbackend.constants.fields.FieldConstants.USER_NAME_FIELD;
import static tran.compbuildbackend.constants.fields.FieldValueConstants.*;
import static tran.compbuildbackend.exceptions.ExceptionUtility.*;

@Service
public class ApplicationUserServiceImpl implements ApplicationUserService {

    private ApplicationUserRepository applicationUserRepository;

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private EmailVerificationTokenRepository emailVerificationTokenRepository;

    private ChangePasswordTokenRepository changePasswordTokenRepository;

    private ApplicationEventPublisher eventPublisher;

    public ApplicationUserServiceImpl(ApplicationUserRepository applicationUserRepository, BCryptPasswordEncoder bCryptPasswordEncoder,
                                      EmailVerificationTokenRepository emailVerificationTokenRepository,
                                      ChangePasswordTokenRepository changePasswordTokenRepository, ApplicationEventPublisher eventPublisher) {
        this.applicationUserRepository = applicationUserRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.emailVerificationTokenRepository = emailVerificationTokenRepository;
        this.changePasswordTokenRepository = changePasswordTokenRepository;

        this.eventPublisher = eventPublisher;
    }

    @Override
    public ApplicationUser persistUser(ApplicationUser newApplicationUser, HttpServletRequest request) {
        checkIfEmailAndUserNameExist(newApplicationUser);

        try {
            newApplicationUser.setPassword(bCryptPasswordEncoder.encode(newApplicationUser.getPassword()));
            // we don't persist or show the confirm password.
            newApplicationUser.setConfirmPassword("");

            newApplicationUser.setEnabled(false); // by default a user account is not yet activated.

            ApplicationUser createdUser = applicationUserRepository.save(newApplicationUser);

            // send an email
            if(request != null) sendSuccessRegistrationEmail(newApplicationUser, request);

            EmailVerificationToken token = getTokenFromUser(createdUser);

            if(token != null) createdUser.setEmailVerificationToken(token);

            return createdUser;
        } catch(Exception ex) {
            throw new GenericRequestException("unhandled exception...");
        }
    }

    @Override
    public void sendSuccessRegistrationEmail(ApplicationUser registeredUser, HttpServletRequest request) {
        if(request != null) {
            String appUrl = request.getScheme() + "://" + request.getServerName() +  ":" + request.getServerPort();
            try {
                eventPublisher.publishEvent(new OnRegistrationSuccessEvent(registeredUser, request.getLocale(), appUrl));
            }
            catch (Exception ex) {
                System.out.println(ex.getMessage());
                applicationUserRepository.delete(registeredUser);
                throwMessageException(USER_NAME_ERROR + registeredUser.getUsername() + GENERIC_USER_NAME_CREATION_ERROR);
            }
        } else {
            throw new GenericRequestException(REQUEST_IS_NULL_ERROR);
        }
    }

    private EmailVerificationToken getTokenFromUser(ApplicationUser user) {
        return emailVerificationTokenRepository.findByUser(user);
    }

    @Override
    public void removeUser(ApplicationUser newApplicationUser) {
        EmailVerificationToken token = emailVerificationTokenRepository.findByUser(newApplicationUser);
        newApplicationUser.setEmailVerificationToken(token);
        applicationUserRepository.delete(newApplicationUser);
    }

    @Transactional
    @Override
    public void enableRegisteredUser(ApplicationUser user) {
        enableAndUpdateUser(user);
        // user is enabled so now delete the registration token required to register the user.
        EmailVerificationToken emailVerificationToken = emailVerificationTokenRepository.findByUser(user);
        if(emailVerificationToken == null) {
            throwTokenException(TOKEN_IS_NOT_PRESENT);
        }
        emailVerificationTokenRepository.deleteByToken(emailVerificationToken.getToken());
    }

    @Override
    public void enableUser(ApplicationUser user) {
        enableAndUpdateUser(user);
    }

    @Override
    public ApplicationUser getUserByEmail(String email, int exceptionType) {
        ApplicationUser user = applicationUserRepository.findByEmail(email);
        if(exceptionType == EXCEPTION_REQUEST_PASSWORD_CHANGE_FAILED && user == null) {
            throwUsernameException(PASSWORD_CANNOT_BE_CHANGED_FOR_INVALID_USER);
        } else if(exceptionType == EXCEPTION_EMAIL_NAME_DOES_NOT_EXIST && user == null) {
            throwMessageException("cannot find user with email '" + email + "'.");
        }
        return user;
    }

    @Override
    public ApplicationUser getUserByUsername(String userName) {
        ApplicationUser user = applicationUserRepository.findByUsername(userName);
        if(user == null) {
            throw new UsernameNotFoundException("the user with '" + userName + "' cannot be found");
        }
        return user;
    }

    private void enableAndUpdateUser(ApplicationUser user) {
        user.setEnabled(true);
        applicationUserRepository.save(user);
    }

    @Transactional
    @Override
    public void changeUserPassword(String newPassword, ApplicationUser user) {
        try {
            user.setPassword(bCryptPasswordEncoder.encode(newPassword));
            applicationUserRepository.save(user);
            ChangePasswordToken token = changePasswordTokenRepository.findByUser(user);
            if(token == null) {
                System.out.println("token is null.....");
                throwTokenException(CANNOT_RETRIEVE_TOKEN);
            }
            changePasswordTokenRepository.deleteByToken(token.getToken());
        } catch (Exception ex) {
            throwTokenException(UNABLE_TO_CHANGE_PASSWORD);
        }
    }

    @Override
    public ApplicationUser sendPasswordChangeEmail(String userName, HttpServletRequest request) {
        if(request == null) {
            throw new GenericRequestException(REQUEST_IS_NULL_ERROR);
        }
        String appUrl = request.getScheme() + "://" + request.getServerName() +  ":" + request.getServerPort();
        ApplicationUser user = getUserByEmail(userName, EXCEPTION_REQUEST_PASSWORD_CHANGE_FAILED);
        try {
            checkForOldToken(user);
            eventPublisher.publishEvent(new OnPasswordResetRequestEvent(user, request.getLocale(), appUrl));
        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());
            throwUsernameException("Password for '" + user.getUsername() + "' cannot be changed at this time");
        }
        ChangePasswordToken token = getChangePasswordTokenFromUser(user);

        if(token != null) user.setChangePasswordToken(token);

        return user;
    }

    private ChangePasswordToken getChangePasswordTokenFromUser(ApplicationUser user) {
        return changePasswordTokenRepository.findByUser(user);
    }

    private void checkForOldToken(ApplicationUser user) {
        ChangePasswordToken token = changePasswordTokenRepository.findByUser(user);
        if(token != null) {
            /*
             * need to delete token because a new one will be generated and this will cause an issue when trying to locate
             * the token for the user.
             */
            changePasswordTokenRepository.deleteByToken(token.getToken());
        }
    }

    private void checkIfEmailAndUserNameExist(ApplicationUser newApplicationUser) {
        /*
         * check if the user name exists and check if the email exists.
         * There are a few cases to consider..
         * 1) The user name exists throw a UsernameDuplicationException.
         * 2) The email exists throw an EmailExistsException.
         * 3) If both exist then throw the MultipleFieldsException.
         */
        boolean userNameExists = checkForUserName(newApplicationUser.getUsername());
        boolean emailExists = checkForEmail(newApplicationUser.getEmail());
        String userNameExistsError = USER_NAME_ERROR + newApplicationUser.getUsername() + ALREADY_EXISTS_ERROR;
        String emailExistsError = EMAIL_ERROR + newApplicationUser.getEmail() + ALREADY_EXISTS_ERROR;
        Map<String, String> errors = new HashMap<>();

        if(userNameExists && emailExists) {
            errors.put(USER_NAME_FIELD, userNameExistsError);
            errors.put(EMAIL_FIELD, emailExistsError);
            throw new MultipleFieldsException(errors);
        } else if(userNameExists) {
            errors.put(USER_NAME_FIELD, userNameExistsError);
            throw new MultipleFieldsException(errors);
        } else if(emailExists) {
            errors.put(EMAIL_FIELD, emailExistsError);
            throw new MultipleFieldsException(errors);
        }
    }

    private boolean checkForUserName(String userName) {
        ApplicationUser user = applicationUserRepository.findByUsername(userName);
        return user != null;
    }

    private boolean checkForEmail(String email) {
        ApplicationUser user = applicationUserRepository.findByEmail(email);
        return user != null;
    }
}
