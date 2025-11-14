package interface_adapter.check_favorite;

import interface_adapter.ViewModel;

public class CheckFavoriteViewModel extends ViewModel<CheckFavoriteState> {

    public static final String VIEW_NAME = "favourite listings";

    public CheckFavoriteViewModel() {
        super(VIEW_NAME);
        this.setState(new CheckFavoriteState());
    }
}
