package use_case.extract_tags;

public class ExtractTagsInputData {
    private final String listingName;
    private final String imageUrl;

    public ExtractTagsInputData(String listingName, String imageUrl) {
        this.listingName = listingName;
        this.imageUrl = imageUrl;
    }

    public String getListingName() {
        return listingName;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}