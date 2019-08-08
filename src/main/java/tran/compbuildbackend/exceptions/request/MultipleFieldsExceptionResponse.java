package tran.compbuildbackend.exceptions.request;


public class MultipleFieldsExceptionResponse {

    private String username;

    private String email;

    private String password;

    private String fullName;

    private String confirmPassword;

    private String placePurchasedAt;

    private String name; // part name or a computer part.

    private String price; // although price is a double we just need an error message for the price input/field.

    private String description;

    private String purchaseDate; // purchase date.

    public MultipleFieldsExceptionResponse() { }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getPlacePurchasedAt() {
        return placePurchasedAt;
    }

    public void setPlacePurchasedAt(String placePurchasedAt) {
        this.placePurchasedAt = placePurchasedAt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
    }
}
