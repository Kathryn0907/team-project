package interface_adapter.listing_detail;

import interface_adapter.ViewModel;

public class ListingDetailViewModel extends ViewModel {
    public static final String VIEW_NAME = "listing_detail";

    private ListingDetailState state = new ListingDetailState();

    public ListingDetailViewModel(){
        super(VIEW_NAME);
    }

    public ListingDetailState getState(){
        return state;
    }

    public void setState(ListingDetailState state){
        this.state = state;
    }
}

