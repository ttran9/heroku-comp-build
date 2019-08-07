package tran.compbuildbackend.domain.computerbuild;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "ComputerPart")
public class ComputerPart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "computerBuildId", nullable = false, updatable = false)
    @JsonIgnore
    private ComputerBuild computerBuild;

    @NotEmpty
    private String name;

    @NotNull
    private double price;

    @Column(unique = true)
    private String uniqueIdentifier;

    @NotNull
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate purchaseDate;

    @NotEmpty
    private String placePurchasedAt;

    @Lob
    private String otherNote;

    public ComputerPart() {}

    public ComputerPart(@NotNull String name, @Min(0) @Max(999999) double price, String uniqueIdentifier,
                        @NotNull LocalDate purchaseDate, @NotNull String placePurchasedAt, String otherNote) {
        this.name = name;
        this.price = price;
        this.uniqueIdentifier = uniqueIdentifier;
        this.purchaseDate = purchaseDate;
        this.placePurchasedAt = placePurchasedAt;
        this.otherNote = otherNote;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ComputerBuild getComputerBuild() {
        return computerBuild;
    }

    public void setComputerBuild(ComputerBuild computerBuild) {
        this.computerBuild = computerBuild;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getPlacePurchasedAt() {
        return placePurchasedAt;
    }

    public void setPlacePurchasedAt(String placePurchasedAt) {
        this.placePurchasedAt = placePurchasedAt;
    }

    public String getOtherNote() {
        return otherNote;
    }

    public void setOtherNote(String otherNote) {
        this.otherNote = otherNote;
    }

    public String getUniqueIdentifier() {
        return uniqueIdentifier;
    }

    public void setUniqueIdentifier(String uniqueIdentifier) {
        this.uniqueIdentifier = uniqueIdentifier;
    }

    @Override
    public String toString() {
        return "ComputerPart{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", uniqueIdentifier='" + uniqueIdentifier + '\'' +
                ", placePurchasedAt='" + placePurchasedAt + '\'' +
                ", otherNote='" + otherNote + '\'' +
                '}';
    }
}
