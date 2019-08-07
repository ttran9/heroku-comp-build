package tran.compbuildbackend.validator;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

public interface MapValidationErrorService {

    /**
     * @param bindingResult An object holding error(s) if any.
     * @return A ResponseEntity with a map containing the error(s).
     */
    ResponseEntity<?> outputCustomError(BindingResult bindingResult);
}
