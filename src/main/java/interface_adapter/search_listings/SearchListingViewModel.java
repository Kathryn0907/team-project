package interface_adapter.search_listings;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class SearchListingViewModel {
    private SearchListingState state = new SearchListingState();
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    public void setState(SearchListingState state) {
        this.state = state;
    }

    public SearchListingState getState() {
        return state;
    }

    public void firePropertyChanged() {
        support.firePropertyChange("state", null, this.state);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }
}