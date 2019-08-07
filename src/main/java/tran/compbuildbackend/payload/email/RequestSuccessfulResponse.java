package tran.compbuildbackend.payload.email;

public class RequestSuccessfulResponse {
    private String message;
    private String token;
    private boolean enabled;

    public RequestSuccessfulResponse() {
    }

    public RequestSuccessfulResponse(String message) {
        this.message = message;
    }

    public RequestSuccessfulResponse(String message, boolean enabled) {
        this.message = message;
        this.enabled = enabled;
    }


    public RequestSuccessfulResponse(String message, String token) {
        this.message = message;
        this.token = token;
    }

    public RequestSuccessfulResponse(String message, String token, boolean enabled) {
        this.message = message;
        this.token = token;
        this.enabled = enabled;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String toString() {
        return "RequestSuccessfulResponse{" +
                "message='" + message + '\'' +
                '}';
    }
}
