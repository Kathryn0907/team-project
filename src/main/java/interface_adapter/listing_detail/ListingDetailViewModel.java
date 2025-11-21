package interface_adapter.listing_detail;

import interface_adapter.ViewModel;

public class ListingDetailViewModel extends ViewModel<ListingDetailState> {
    public static final String VIEW_NAME = "listing_detail";

    private static final ListingDetailViewModel instance = new ListingDetailViewModel();

    public static ListingDetailViewModel getInstance() {
        return instance;
    }

    private ListingDetailState state = new ListingDetailState();

    private ListingDetailViewModel(){
        super(VIEW_NAME);
    }

    public ListingDetailState getState(){
        return state;
    }

    public void setState(ListingDetailState state){
        this.state = state;
    }
}

