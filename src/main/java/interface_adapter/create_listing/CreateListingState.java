package interface_adapter.create_listing;

import Entities.Listing;

import java.util.ArrayList;
import java.util.List;

/**
 * The state for the Create Listing View Model.
 */
public class CreateListingState {

    private String name = "";
    private String description = "";
    private String price = "";
    private String address = "";
    private String area = "";
    private String bedrooms = "";
    private String bathrooms = "";
    private Listing.BuildingType buildingType = Listing.BuildingType.OTHER;
    private List<String> tags = new ArrayList<>();
    private String photoBase64 = "";

    private String nameError;
    private String priceError;
    private String addressError;
    private String photoError;


    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getPrice() { return price; }
    public String getAddress() { return address; }
    public String getArea() { return area; }
    public String getBedrooms() { return bedrooms; }
    public String getBathrooms() { return bathrooms; }
    public Listing.BuildingType getBuildingType() { return buildingType; }
    public String getPhotoBase64() { return photoBase64; }
    public String getNameError() { return nameError; }
    public String getPriceError() { return priceError; }
    public String getAddressError() { return addressError; }
    public String getPhotoError() { return photoError; }
    public List<String> getTags() { return tags; }

    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setPrice(String price) { this.price = price; }
    public void setAddress(String address) { this.address = address; }
    public void setArea(String area) { this.area = area; }
    public void setBedrooms(String bedrooms) { this.bedrooms = bedrooms; }
    public void setBathrooms(String bathrooms) { this.bathrooms = bathrooms; }
    public void setBuildingType(Listing.BuildingType type) { this.buildingType = type; }
    public void setPhotoBase64(String photoBase64) { this.photoBase64 = photoBase64; }
    public void setNameError(String nameError) { this.nameError = nameError; }
    public void setPriceError(String priceError) { this.priceError = priceError; }
    public void setAddressError(String addressError) { this.addressError = addressError; }
    public void setPhotoError(String photoError) { this.photoError = photoError; }
    public void setTags(List<String> tags) { this.tags = tags; }

    @Override
    public String toString() {
        return "CreateListingState{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price='" + price + '\'' +
                ", address='" + address + '\'' +
                ", area='" + area + '\'' +
                ", bedrooms='" + bedrooms + '\'' +
                ", bathrooms='" + bathrooms + '\'' +
                ", buildingType=" + buildingType +
                ", photoBase64='" + photoBase64 + '\'' +
                '}';
    }
}
