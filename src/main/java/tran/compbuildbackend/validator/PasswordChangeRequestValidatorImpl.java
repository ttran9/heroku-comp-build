package tran.compbuildbackend.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import tran.compbuildbackend.payload.email.PasswordChangeRequest;

import static tran.compbuildbackend.constants.fields.ErrorKeyConstants.LENGTH_KEY;
import static tran.compbuildbackend.constants.fields.ErrorKeyConstants.MATCH_KEY;
import static tran.compbuildbackend.constants.fields.FieldConstants.CONFIRM_PASSWORD_FIELD;
import static tran.compbuildbackend.constants.fields.FieldConstants.PASSWORD_FIELD;
import static tran.compbuildbackend.constants.fields.FieldValueConstants.PASSWORD_MISMATCH_ERROR;
import static tran.compbuildbackend.constants.fields.FieldValueConstants.SHORT_PASSWORD_ERROR;

@Component
public class PasswordChangeRequestValidatorImpl implements PasswordChangeRequestValidator {
    @Override
    public boolean supports(Class<?> aClass) {
        /*
         * we are supporting our ApplicationUser class.
         * we are further validating that we have the correct object.
         */
        return PasswordChangeRequest.class.equals(aClass);
    }

    @Override
    public void validate(Object object, Errors errors) {
        PasswordChangeRequest passwordChangeRequest = (PasswordChangeRequest) object;

        if(passwordChangeRequest.getPassword().length() < 6) {
            errors.rejectValue(PASSWORD_FIELD, LENGTH_KEY, SHORT_PASSWORD_ERROR);
        }

        if(!passwordChangeRequest.getPassword().equals(passwordChangeRequest.getConfirmPassword())) {
            errors.rejectValue(CONFIRM_PASSWORD_FIELD, MATCH_KEY, PASSWORD_MISMATCH_ERROR);
        }
    }
}
