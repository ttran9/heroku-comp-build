package tran.compbuildbackend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import tran.compbuildbackend.exceptions.request.GenericRequestException;
import tran.compbuildbackend.exceptions.request.GenericRequestExceptionResponse;
import tran.compbuildbackend.exceptions.request.MultipleFieldsException;
import tran.compbuildbackend.exceptions.request.MultipleFieldsExceptionResponse;
import tran.compbuildbackend.exceptions.security.InvalidLoginExceptionResponse;

import java.util.Map;

import static tran.compbuildbackend.constants.fields.FieldConstants.*;

@ControllerAdvice
@RestController
public class CustomResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler
    public final ResponseEntity<Object> handleInvalidLogin(BadCredentialsException ex) {
        InvalidLoginExceptionResponse invalidLoginExceptionResponse = new InvalidLoginExceptionResponse();
        return new ResponseEntity<>(invalidLoginExceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public final ResponseEntity<Object> handleDisabledAccountLogin(DisabledException ex) {
        InvalidLoginExceptionResponse invalidLoginExceptionResponse = new InvalidLoginExceptionResponse();
        return new ResponseEntity<>(invalidLoginExceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public final ResponseEntity<Object> handleGenericRequestException(GenericRequestException ex) {
        GenericRequestExceptionResponse response = new GenericRequestExceptionResponse();

        if(ex.getMessage() != null) {
            response.setMessage(ex.getMessage());
        }
        if(ex.getUsername() != null) {
            response.setUsername(ex.getUsername());
        }
        if(ex.getToken() != null) {
            response.setToken(ex.getToken());
        }
        if(ex.getPassword() != null) {
            response.setPassword(ex.getPassword());
        }

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public final ResponseEntity<Object> handleMultipleFieldsException(MultipleFieldsException ex) {
        Map<String, String> errors = ex.getMapError();
        MultipleFieldsExceptionResponse response = new MultipleFieldsExceptionResponse();

        if(errors.get(USER_NAME_FIELD) != null) {
            response.setUsername(errors.get(USER_NAME_FIELD));
        }
        if(errors.get(EMAIL_FIELD) != null) {
            response.setEmail(errors.get(EMAIL_FIELD));
        }
        if(errors.get(FULL_NAME_FIELD) != null) {
            response.setFullName(errors.get(FULL_NAME_FIELD));
        }
        if(errors.get(PASSWORD_FIELD) != null) {
            response.setPassword(errors.get(PASSWORD_FIELD));
        }
        if(errors.get(CONFIRM_PASSWORD_FIELD) != null) {
            response.setConfirmPassword(errors.get(CONFIRM_PASSWORD_FIELD));
        }
        if(errors.get(PLACE_PURCHASED_AT_FIELD) != null) {
            response.setPlacePurchasedAt(errors.get(PLACE_PURCHASED_AT_FIELD));
        }
        if(errors.get(NAME_FIELD) != null) {
            response.setName(errors.get(NAME_FIELD));
        }
        if(errors.get(PRICE_FIELD) != null) {
            response.setPrice(errors.get(PRICE_FIELD));
        }
        if(errors.get(DESCRIPTION_FIELD) != null) {
            response.setDescription(errors.get(DESCRIPTION_FIELD));
        }
        if(errors.get(PURCHASE_DATE_FIELD) != null) {
            response.setPurchaseDate(errors.get(PURCHASE_DATE_FIELD));
        }
        if(errors.get(BUILD_DESCRIPTION_FIELD) != null) {
            response.setBuildDescription(errors.get(BUILD_DESCRIPTION_FIELD));
        }

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
