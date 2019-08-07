package tran.compbuildbackend.payload.computerbuild;

public class ComputerBuildResponse {

    private String name;
    private String message;
    private String buildIdentifier;
    private String buildDescription;

    public ComputerBuildResponse() { }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getBuildIdentifier() {
        return buildIdentifier;
    }

    public void setBuildIdentifier(String buildIdentifier) {
        this.buildIdentifier = buildIdentifier;
    }

    public String getBuildDescription() {
        return buildDescription;
    }

    public void setBuildDescription(String buildDescription) {
        this.buildDescription = buildDescription;
    }

    @Override
    public String toString() {
        return "ComputerBuildResponse{" +
                "name='" + name + '\'' +
                '}';
    }
}
