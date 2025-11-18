package interface_adapter.save_favorite;

import interface_adapter.ViewModel;

public class SaveFavoriteViewModel extends ViewModel<SaveFavoriteState> {

    public static final String VIEW_NAME = "save favorite";

    public SaveFavoriteViewModel() {
        super(VIEW_NAME);
        this.setState(new SaveFavoriteState());
    }
}
