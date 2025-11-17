package interface_adapter.messaging;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class MessageViewModel {
    private MessageState state = new MessageState();
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    public void setState(MessageState state) {
        this.state = state;
    }

    public MessageState getState() {
        return state;
    }

    public void firePropertyChanged() {
        support.firePropertyChange("state", null, this.state);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }
}