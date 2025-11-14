package interface_adapter.extract_tags;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class ExtractTagsViewModel {
    private ExtractTagsState state = new ExtractTagsState();
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    public void setState(ExtractTagsState state) {
        this.state = state;
    }

    public ExtractTagsState getState() {
        return state;
    }

    public void firePropertyChanged() {
        support.firePropertyChange("state", null, this.state);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }
}