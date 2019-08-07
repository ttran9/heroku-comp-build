package tran.compbuildbackend.validator;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import tran.compbuildbackend.exceptions.request.MultipleFieldsException;

import java.util.HashMap;
import java.util.Map;

@Service
public class MapValidationErrorServiceImpl implements MapValidationErrorService {

    @Override
    public ResponseEntity<?> outputCustomError(BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {

            Map<String, String> errorMap = new HashMap<>();

            for (FieldError fieldError: bindingResult.getFieldErrors()) {
                errorMap.put(fieldError.getField(), fieldError.getDefaultMessage());
            }

            throw new MultipleFieldsException(errorMap);
        }
        return null; // no errors so return nothing.
    }
}
