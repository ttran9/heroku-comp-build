package tran.compbuildbackend.controllers.applicationusers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import tran.compbuildbackend.constants.mapping.MappingConstants;
import tran.compbuildbackend.domain.user.ApplicationUser;
import tran.compbuildbackend.payload.email.InitialPasswordChangeRequest;
import tran.compbuildbackend.payload.email.LoginRequest;
import tran.compbuildbackend.payload.email.PasswordChangeRequest;
import tran.compbuildbackend.payload.email.RequestSuccessfulResponse;
import tran.compbuildbackend.security.JwtTokenProvider;
import tran.compbuildbackend.services.security.ApplicationUserAuthenticationService;
import tran.compbuildbackend.services.users.ApplicationUserService;
import tran.compbuildbackend.services.verificationtoken.ChangePasswordTokenServiceImpl;
import tran.compbuildbackend.services.verificationtoken.EmailVerificationTokenServiceImpl;
import tran.compbuildbackend.validator.ApplicationUserValidator;
import tran.compbuildbackend.validator.MapValidationErrorService;
import tran.compbuildbackend.validator.PasswordChangeRequestValidator;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static tran.compbuildbackend.constants.mapping.MappingConstants.*;
import static tran.compbuildbackend.constants.messages.ResponseMessage.*;


@RestController
@RequestMapping(USERS_API)
public class ApplicationUserController {

    private ApplicationUserService applicationUserService;

    private MapValidationErrorService mapValidationErrorService;

    private ApplicationUserValidator applicationUserValidator;

    private PasswordChangeRequestValidator passwordChangeRequestValidator;

    private JwtTokenProvider jwtTokenProvider;

    private AuthenticationManager authenticationManager;

    private ApplicationUserAuthenticationService authenticationService;

    private EmailVerificationTokenServiceImpl emailVerificationService;

    private ChangePasswordTokenServiceImpl changePasswordTokenService;

    @Autowired
    public ApplicationUserController(ApplicationUserService applicationUserService, MapValidationErrorService mapValidationErrorService,
                                     ApplicationUserValidator applicationUserValidator, JwtTokenProvider jwtTokenProvider,
                                     AuthenticationManager authenticationManager, ApplicationUserAuthenticationService authenticationService,
                                     EmailVerificationTokenServiceImpl emailVerificationService, ChangePasswordTokenServiceImpl changePasswordTokenService,
                                     PasswordChangeRequestValidator passwordChangeRequestValidator) {
        this.applicationUserService = applicationUserService;
        this.mapValidationErrorService = mapValidationErrorService;
        this.applicationUserValidator = applicationUserValidator;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
        this.authenticationService = authenticationService;
        this.emailVerificationService = emailVerificationService;
        this.changePasswordTokenService = changePasswordTokenService;
        this.passwordChangeRequestValidator = passwordChangeRequestValidator;
    }

    @PostMapping(LOGIN_URL)
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest, BindingResult bindingResult) {
        mapValidationErrorService.outputCustomError(bindingResult);

        return authenticationService.applicationUserAuthentication(loginRequest, authenticationManager, jwtTokenProvider);
    }

    @PostMapping(REGISTER_URL)
    public ResponseEntity<?> registerUser(@Valid @RequestBody ApplicationUser applicationUser, BindingResult bindingResult,
                                          HttpServletRequest request) {
        // validate that passwords match.
        applicationUserValidator.validate(applicationUser, bindingResult);

        mapValidationErrorService.outputCustomError(bindingResult);

        ApplicationUser newApplicationUser = applicationUserService.persistUser(applicationUser, request);

        RequestSuccessfulResponse userResponse = new RequestSuccessfulResponse(VERIFY_EMAIL_MESSAGE,
                newApplicationUser.getEmailVerificationToken().getToken(), newApplicationUser.isEnabled());

        return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
    }

    @PostMapping(MappingConstants.CONFIRM_REGISTRATION_URL + TOKEN_PARAM)
    public ResponseEntity<?> confirmRegistration(@PathVariable String token) {
        ApplicationUser user = emailVerificationService.validateVerificationToken(token);
        applicationUserService.enableRegisteredUser(user);

        return new ResponseEntity<>(new RequestSuccessfulResponse(SUCCESSFUL_ACCOUNT_ACTIVATION, user.isEnabled()), HttpStatus.OK);
    }

    @PostMapping(MappingConstants.CHANGE_PASSWORD_URL + TOKEN_PARAM)
    public ResponseEntity<?> processChangeUserPassword(@Valid @RequestBody PasswordChangeRequest passwordChangeRequest, BindingResult bindingResult,
                                                       @PathVariable String token) {
        // process the password change request.
        // validate that passwords match.
        passwordChangeRequestValidator.validate(passwordChangeRequest, bindingResult);

        mapValidationErrorService.outputCustomError(bindingResult);

        ApplicationUser user = changePasswordTokenService.validateVerificationToken(token);

        applicationUserService.changeUserPassword(passwordChangeRequest.getPassword(), user);

        return new ResponseEntity<>(new RequestSuccessfulResponse(PASSWORD_CHANGED), HttpStatus.OK);
    }

    @PostMapping(MappingConstants.CHANGE_PASSWORD_URL)
    public ResponseEntity<?> requestChangeUserPassword(@Valid @RequestBody InitialPasswordChangeRequest passwordChangeRequest,
                                                       BindingResult bindingResult, HttpServletRequest request) {
        // requesting to change user password.
        mapValidationErrorService.outputCustomError(bindingResult);

        ApplicationUser user = applicationUserService.sendPasswordChangeEmail(passwordChangeRequest.getUsername(), request);

        RequestSuccessfulResponse userResponse = new RequestSuccessfulResponse(CHANGE_PASSWORD,
                user.getChangePasswordToken().getToken());

        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }
}
