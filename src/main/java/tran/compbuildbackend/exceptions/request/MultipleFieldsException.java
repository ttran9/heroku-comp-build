package tran.compbuildbackend.exceptions.request;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Map;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MultipleFieldsException extends RuntimeException{

    private Map<String, String> mapError;

    public MultipleFieldsException(Map<String, String> errors) {
        this.mapError = errors;
    }

    public Map<String, String> getMapError() {
        return mapError;
    }

    public void setMapError(Map<String, String> mapError) {
        this.mapError = mapError;
    }
}
