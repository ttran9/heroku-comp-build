package tran.compbuildbackend.domain.computerbuild;

import com.fasterxml.jackson.annotation.JsonIgnore;
import tran.compbuildbackend.domain.user.ApplicationUser;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name="ComputerBuild")
public class ComputerBuild {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min=2, max=40)
    @Column(updatable = false)
    private String name;

    @NotBlank
    private String buildDescription;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    private ApplicationUser user;

    private LocalDate createdAt;

    private LocalDate updatedAt;

    // a list of directions to complete this build.
    @OneToMany(cascade = CascadeType.REFRESH, orphanRemoval = true, mappedBy = "computerBuild", fetch = FetchType.LAZY)
    private List<Direction> directions = new LinkedList<>();

    private Integer directionsCount = 0;

    // a list of notes for when the computer was being built.
    @OneToMany(cascade = CascadeType.REFRESH, orphanRemoval = true, mappedBy = "computerBuild", fetch = FetchType.LAZY)
    private List<BuildNote> buildNotes = new LinkedList<>();

    private Integer buildNotesCount = 0;

    // a list of notes when attempting to overclock this computer build.
    @OneToMany(cascade = CascadeType.REFRESH, orphanRemoval = true, mappedBy = "computerBuild", fetch = FetchType.LAZY)
    private List<OverclockingNote> overclockingNotes = new LinkedList<>();

    private Integer overclockingNotesCount = 0;

    // a list of computer parts.
    @OneToMany(cascade = CascadeType.REFRESH, orphanRemoval = true, mappedBy = "computerBuild", fetch = FetchType.LAZY)
    private List<ComputerPart> computerParts = new LinkedList<>();

    private Integer computerPartsCount = 0;

    // a list of items that discusses the purpose of this build.
    @OneToMany(cascade = CascadeType.REFRESH, orphanRemoval = true, mappedBy = "computerBuild", fetch = FetchType.LAZY)
    private List<Purpose> purposeList = new LinkedList<>();

    private Integer purposeCount = 0;

    @Column(unique = true)
    private String buildIdentifier;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDate.now();
    }

    @PostPersist
    protected void onUpdate() {
        this.updatedAt = LocalDate.now();
    }

    public ComputerBuild() { }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ApplicationUser getUser() {
        return user;
    }

    public void setUser(ApplicationUser user) {
        this.user = user;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDate getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<Direction> getDirections() {
        return directions;
    }

    public void setDirections(List<Direction> directions) {
        this.directions = directions;
    }

    public Integer getDirectionsCount() {
        return directionsCount;
    }

    public void setDirectionsCount(Integer directionsCount) {
        this.directionsCount = directionsCount;
    }

    public List<BuildNote> getBuildNotes() {
        return buildNotes;
    }

    public void setBuildNotes(List<BuildNote> buildNotes) {
        this.buildNotes = buildNotes;
    }

    public Integer getBuildNotesCount() {
        return buildNotesCount;
    }

    public void setBuildNotesCount(Integer buildNotesCount) {
        this.buildNotesCount = buildNotesCount;
    }

    public List<OverclockingNote> getOverclockingNotes() {
        return overclockingNotes;
    }

    public void setOverclockingNotes(List<OverclockingNote> overclockingNotes) {
        this.overclockingNotes = overclockingNotes;
    }

    public Integer getOverclockingNotesCount() {
        return overclockingNotesCount;
    }

    public void setOverclockingNotesCount(Integer overclockingNotesCount) {
        this.overclockingNotesCount = overclockingNotesCount;
    }

    public List<ComputerPart> getComputerParts() {
        return computerParts;
    }

    public void setComputerParts(List<ComputerPart> computerParts) {
        this.computerParts = computerParts;
    }

    public Integer getComputerPartsCount() {
        return computerPartsCount;
    }

    public void setComputerPartsCount(Integer computerPartsCount) {
        this.computerPartsCount = computerPartsCount;
    }

    public List<Purpose> getPurposeList() {
        return purposeList;
    }

    public void setPurposeList(List<Purpose> purposeList) {
        this.purposeList = purposeList;
    }

    public Integer getPurposeCount() {
        return purposeCount;
    }

    public void setPurposeCount(Integer purposeCount) {
        this.purposeCount = purposeCount;
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
        return "ComputerBuild{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", buildDescription='" + buildDescription + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", buildIdentifier='" + buildIdentifier + '\'' +
                '}';
    }
}
