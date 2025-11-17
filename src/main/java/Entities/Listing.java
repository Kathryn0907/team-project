package Entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a single Airbnb-style listing.
 * Each listing may have multiple Imagga tags that describe its images,
 * and multiple main categories derived from high-confidence tags.
 */
public class Listing {

    // ------------ Enum for Building Type (Dropdown) ------------
    public enum BuildingType {
        CONDO,
        HOUSE,
        APARTMENT,
        VILLA,
        TOWNHOUSE,
        STUDIO,
        DUPLEX,
        OTHER
    }

    // ------------ Fields ------------

    private String name;
    private User owner;
    private String photoPath;

    // Imagga-related
    private List<String> tags;
    private List<String> mainCategories;

    private String description;
    private double price;

    // Google API Location
    private String address;
    private double distance;

    private double area;
    private int bedrooms;
    private int bathrooms;

    private BuildingType buildingType;

    private boolean active;
    private List<Comment> comments;


    public Listing(String name,
                   User owner,
                   String photoPath,
                   List<String> tags,
                   List<String> mainCategories,
                   String description,
                   double price,
                   String address,
                   double distance,
                   double area,
                   int bedrooms,
                   int bathrooms,
                   BuildingType buildingType,  // NEW
                   boolean active) {

        if ("".equals(name)) {
            throw new IllegalArgumentException("Name of listing cannot be empty");
        }
        // if (owner == null) {
            // throw new IllegalArgumentException("Owner cannot be empty");
        //}
        // This code will be included in the final iteration, this is just for the demo
        if ("".equals(description)) {
            throw new IllegalArgumentException("Description cannot be empty");
        }

        this.name = name;
        this.owner = owner;
        this.photoPath = photoPath;

        this.tags = tags != null ? new ArrayList<>(tags) : new ArrayList<>();
        this.mainCategories = mainCategories != null ? new ArrayList<>(mainCategories) : new ArrayList<>();

        this.description = description;
        this.price = price;

        this.address = address;
        this.distance = distance;
        this.area = area;
        this.bedrooms = bedrooms;
        this.bathrooms = bathrooms;

        this.buildingType = buildingType;

        this.active = active;
        this.comments = new ArrayList<>();
    }

    public Listing() {
        this.tags = new ArrayList<>();
        this.mainCategories = new ArrayList<>();
        this.comments = new ArrayList<>();
        this.buildingType = BuildingType.OTHER; // default value
    }


    // ------------ Getters & Setters ------------

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String getPhotoPath() { return photoPath; }

    public void setPhotoPath(String photoPath) { this.photoPath = photoPath; }

    public List<String> getTags() {
        return Collections.unmodifiableList(tags);
    }

    public void setTags(List<String> tags) {
        this.tags = tags != null ? new ArrayList<>(tags) : new ArrayList<>();
    }

    public List<String> getMainCategories() {
        return Collections.unmodifiableList(mainCategories);
    }

    public void setMainCategories(List<String> mainCategories) {
        this.mainCategories = mainCategories != null ? new ArrayList<>(mainCategories) : new ArrayList<>();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }

    public int getBedrooms() {
        return bedrooms;
    }

    public void setBedrooms(int bedrooms) {
        this.bedrooms = bedrooms;
    }

    public int getBathrooms() {
        return bathrooms;
    }

    public void setBathrooms(int bathrooms) {
        this.bathrooms = bathrooms;
    }

    // ---------- NEW BuildingType Getter/Setter ----------
    public BuildingType getBuildingType() {
        return buildingType;
    }

    public void setBuildingType(BuildingType buildingType) {
        this.buildingType = buildingType;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<Comment> getComments() {
        return Collections.unmodifiableList(comments);
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments != null ? new ArrayList<>(comments) : new ArrayList<>();
    }


    // ------------ Convenience methods ------------

    public void addTag(String tag) {
        this.tags.add(tag);
    }

    public void addCategory(String category) {
        this.mainCategories.add(category);
    }

    public void addComment(Comment comment) {
        this.comments.add(comment);
    }

    public void activate() {
        this.active = true;
    }

    public void deactivate() {
        this.active = false;
    }
}