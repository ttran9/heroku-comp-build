package tran.compbuildbackend.payload.computerbuild;

public class ComputerBuildDetailResponse {

    private String message;
    private String uniqueIdentifier;
    private double deletedItemPrice;

    public ComputerBuildDetailResponse() { }

    public ComputerBuildDetailResponse(String message, String uniqueIdentifier) {
        this.message = message;
        this.uniqueIdentifier = uniqueIdentifier;
    }

    public ComputerBuildDetailResponse(String message, String uniqueIdentifier, double deletedItemPrice) {
        this.message = message;
        this.uniqueIdentifier = uniqueIdentifier;
        this.deletedItemPrice = deletedItemPrice;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUniqueIdentifier() {
        return uniqueIdentifier;
    }

    public void setUniqueIdentifier(String uniqueIdentifier) {
        this.uniqueIdentifier = uniqueIdentifier;
    }

    public double getDeletedItemPrice() {
        return deletedItemPrice;
    }

    public void setDeletedItemPrice(double deletedItemPrice) {
        this.deletedItemPrice = deletedItemPrice;
    }
}
