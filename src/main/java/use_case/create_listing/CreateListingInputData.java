package use_case.create_listing;

import Entities.Listing;
import Entities.User;

/*
    The Input Data for the Create Listing Use Case
 */

public class CreateListingInputData {

    private final String name;
    private final String description;
    private final User owner;
    private final double price;
    private final String address;
    private final double area;
    private final int bedrooms;
    private final int bathrooms;
    private final Listing.BuildingType buildingType;
    private final String photoPath;

    public CreateListingInputData(String name,
                                  String description,
                                  User owner,
                                  double price,
                                  String address,
                                  double area,
                                  int bedrooms,
                                  int bathrooms,
                                  Listing.BuildingType buildingType,
                                  String photoPath) {
        this.name = name;
        this.description = description;
        this.owner = owner;
        this.price = price;
        this.address = address;
        this.area = area;
        this.bedrooms = bedrooms;
        this.bathrooms = bathrooms;
        this.buildingType = buildingType;
        this.photoPath = photoPath;
    }

    String getName() { return name; }
    String getDescription() { return description; }
    User getOwner() { return owner; }
    double getPrice() { return price; }
    String getAddress() { return address; }
    double getArea() { return area; }
    int getBedrooms() { return bedrooms; }
    int getBathrooms() { return bathrooms; }
    Listing.BuildingType getBuildingType() { return buildingType; }
    String getPhotoPath() { return photoPath; }
}
