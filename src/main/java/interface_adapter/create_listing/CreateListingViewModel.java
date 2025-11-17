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

    public CreateListingViewModel() {
        super("create listing");
        setState(new CreateListingState());
    }
}
