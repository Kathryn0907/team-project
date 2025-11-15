package use_case.create_listing;

import Entities.Comments;
import Entities.Listing;
import Entities.User;

import java.util.List;

/*
    The Input Data for the Create Listing Use Case
 */

public class CreateListingInputData {

    private String name;
    private User owner;
    private String photoPath;

    private List<String> tags;
    private List<String> mainCategories;

    private String description;
    private double price;

    private String address;
    private double distance;

    private double area;
    private int bedrooms;
    private int bathrooms;

    private Listing.BuildingType buildingType;

    private boolean active;
    private List<Comments> comments;

    public CreateListingInputData(String name,
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
                                  Listing.BuildingType buildingType,  // NEW
                                  boolean active) {
        this.name = name;
        this.owner = owner;
        this.photoPath = photoPath;
        this.tags = tags;
        this.mainCategories = mainCategories;
        this.description = description;
        this.price = price;
        this.address = address;
        this.distance = distance;
        this.area = area;
        this.bedrooms = bedrooms;
        this.bathrooms = bathrooms;
        this.buildingType = buildingType;
        this.active = active;
    }

    String getName() { return name; }
    User getOwner() { return owner; }
    String getPhotoPath() { return photoPath; }
    List<String> getTags() { return tags; }
    List<String> getMainCategories() { return mainCategories; }
    String getDescription() { return description; }
    double getPrice() { return price; }
    String getAddress() { return address; }
    double getDistance() { return distance; }
    double getArea() { return area; }
    int getBedrooms() { return bedrooms; }
    int getBathrooms() { return bathrooms; }
    Listing.BuildingType getBuildingType() { return buildingType; }
    public boolean isActive() {return active; }
}
