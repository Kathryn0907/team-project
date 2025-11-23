package interface_adapter.create_listing;

import interface_adapter.ViewModel;

/**
 * The ViewModel for the CreateListing View.
 */

public class CreateListingViewModel extends ViewModel<CreateListingState> {

    public static final String TITLE_LABEL = "Create Listing View";
    public static final String NAME_LABEL = "Listing Name";
    public static final String DESCRIPTION_LABEL = "Property Description";
    public static final String PHOTO_LABEL = "Upload Photo";

    public static final String CREATE_BUTTON_LABEL = "Create Listing";
    public static final String CANCEL_BUTTON_LABEL = "Cancel";
    public static final String PRICE_LABEL = "Price (CAD)";
    public static final String ADDRESS_LABEL = "Address";
    public static final String AREA_LABEL = "Area (sq ft)";
    public static final String BEDROOMS_LABEL = "Number of Bedrooms";
    public static final String BATHROOMS_LABEL = "Number of Bathrooms";
    public static final String BUILDING_TYPE_LABEL = "Building Type";

    public CreateListingViewModel() {
        super("create listing");
        setState(new CreateListingState());
    }
}
