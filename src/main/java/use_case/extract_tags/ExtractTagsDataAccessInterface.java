package use_case.extract_tags;

import Entities.Listing;

public interface ExtractTagsDataAccessInterface {
    Listing getListingByName(String name);
    void saveListing(Listing listing);
}