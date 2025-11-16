package use_case.create_listing;

import Entities.User;

public class CreateListingOutputData {
    private final User owner;
    private final String name;

    public CreateListingOutputData(User owner, String title) {
        this.owner = owner;
        this.name = name;
    }
    public User getOwner() {
        return owner;
    }

    public String getName() {
        return name;
    }

}
