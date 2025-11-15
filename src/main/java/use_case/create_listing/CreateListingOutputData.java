package use_case.create_listing;

import Entities.User;

public class CreateListingOutputData {
    private final User owner;
    private final String name;
    private final String photoPath;

    public CreateListingOutputData(User owner, String name, String photoPath) {
        this.owner = owner;
        this.name = name;
        this.photoPath = photoPath;
    }
    public User getOwner() {
        return owner;
    }

    public String getName() {
        return name;
    }

    public String getPhotoPath() { return photoPath;}

}
