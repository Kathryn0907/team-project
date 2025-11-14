package interface_adapter.my_listings;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class MyListingsViewModel {
    private MyListingsState state = new MyListingsState();
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    public void setState(MyListingsState state) {
        this.state = state;
    }

    public MyListingsState getState() {
        return state;
    }

    public void firePropertyChanged() {
        support.firePropertyChange("state", null, this.state);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }
}