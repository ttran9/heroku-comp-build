package tran.compbuildbackend.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import tran.compbuildbackend.domain.user.ApplicationUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static tran.compbuildbackend.constants.fields.ErrorKeyConstants.*;
import static tran.compbuildbackend.constants.fields.FieldConstants.*;
import static tran.compbuildbackend.constants.fields.FieldValueConstants.*;

@Component
public class ApplicationUserValidator implements Validator {

    private static final String USERNAME_PATTERN = "^[a-z0-9]{3,15}$";

    @Override
    public boolean supports(Class<?> aClass) {
        /*
         * we are supporting our ApplicationUser class.
         * we are further validating that we have the correct object.
         */
        return ApplicationUser.class.equals(aClass);
    }

    @Override
    public void validate(Object object, Errors errors) {
        ApplicationUser applicationUser = (ApplicationUser) object;

        if(applicationUser.getPassword().length() < 6) {
            errors.rejectValue(PASSWORD_FIELD, LENGTH_KEY, SHORT_PASSWORD_ERROR);
        }

        if(!applicationUser.getPassword().equals(applicationUser.getConfirmPassword())) {
            errors.rejectValue(CONFIRM_PASSWORD_FIELD, MATCH_KEY, PASSWORD_MISMATCH_ERROR);
        }


        if(!verifyUserName(applicationUser.getUsername())) {
            errors.rejectValue(USER_NAME_FIELD, INVALID_KEY, USER_NAME_CREATION_ERROR);
        }
    }

    private boolean verifyUserName(String userName) {
        Pattern pattern;
        Matcher matcher;

        pattern = Pattern.compile(USERNAME_PATTERN);

        matcher = pattern.matcher(userName);
        return matcher.matches();
    }
}
