package tran.compbuildbackend.dto.computerbuild;

import tran.compbuildbackend.domain.computerbuild.*;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

/*
 * A data transfer object as to not expose the ApplicationUser object that owns the original ComputerBuild object that
 * this object is mapped from.
 */
public class ComputerBuildDto {

    // the name of the computer build.
    private String name;

    // the brief description of the computer build.
    private String buildDescription;

    // username of the owner of this computer build.
    private String username;

    // the date the computer build was created at.
    private LocalDateTime createdAt;

    // the last date the computer build was updated at.
    private LocalDateTime updatedAt;

    // a list of directions to complete this build.
    private List<Direction> directions = new LinkedList<>();

    // a list of notes for when the computer was being built.
    private List<BuildNote> buildNotes = new LinkedList<>();

    // a list of notes when attempting to overclock this computer build.
    private List<OverclockingNote> overclockingNotes = new LinkedList<>();

    // a list of computer parts.
    private List<ComputerPart> computerParts = new LinkedList<>();

    // a list of items that discusses the purpose of this build.
    private List<Purpose> purposeList = new LinkedList<>();

    // the unique of the ComputerBuild
    private String buildIdentifier;

    // the total price of the computer build.
    private Double totalPrice;

    public ComputerBuildDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBuildDescription() {
        return buildDescription;
    }

    public void setBuildDescription(String buildDescription) {
        this.buildDescription = buildDescription;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<Direction> getDirections() {
        return directions;
    }

    public void setDirections(List<Direction> directions) {
        this.directions = directions;
    }

    public List<BuildNote> getBuildNotes() {
        return buildNotes;
    }

    public void setBuildNotes(List<BuildNote> buildNotes) {
        this.buildNotes = buildNotes;
    }

    public List<OverclockingNote> getOverclockingNotes() {
        return overclockingNotes;
    }

    public void setOverclockingNotes(List<OverclockingNote> overclockingNotes) {
        this.overclockingNotes = overclockingNotes;
    }

    public List<ComputerPart> getComputerParts() {
        return computerParts;
    }

    public void setComputerParts(List<ComputerPart> computerParts) {
        this.computerParts = computerParts;
    }

    public List<Purpose> getPurposeList() {
        return purposeList;
    }

    public void setPurposeList(List<Purpose> purposeList) {
        this.purposeList = purposeList;
    }

    public String getBuildIdentifier() {
        return buildIdentifier;
    }

    public void setBuildIdentifier(String buildIdentifier) {
        this.buildIdentifier = buildIdentifier;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
