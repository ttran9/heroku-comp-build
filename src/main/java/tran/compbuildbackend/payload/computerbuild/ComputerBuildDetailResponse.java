package tran.compbuildbackend.payload.computerbuild;

public class ComputerBuildDetailResponse {

    private String message;

    public ComputerBuildDetailResponse() { }

    public ComputerBuildDetailResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
