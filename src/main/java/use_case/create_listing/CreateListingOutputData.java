package use_case.create_listing;

import Entities.User;

public class CreateListingOutputData {
    // private final User owner;
    private final String name;
    private final String photoPath;
    private final String description;

    public CreateListingOutputData(String name, String description, String photoPath) {
        // this.owner = owner;
        this.name = name;
        this.description = description;
        this.photoPath = photoPath;
    }
    /**public User getOwner() {
        return owner;
    }
    **/
    public String getName() { return name;}
    public String getDescription() { return description; }
    public String getPhotoPath() { return photoPath;}

}
