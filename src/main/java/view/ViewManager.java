package view;

import interface_adapter.ViewManagerModel;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * The View Manager for the program. It listens for property change events
 * in the ViewManagerModel and updates which View should be visible.
 */
public class ViewManager implements PropertyChangeListener {
    private final CardLayout cardLayout;
    private final JPanel views;
    private final ViewManagerModel viewManagerModel;

    public ViewManager(JPanel views, CardLayout cardLayout, ViewManagerModel viewManagerModel) {
        this.views = views;
        this.cardLayout = cardLayout;
        this.viewManagerModel = viewManagerModel;
        this.viewManagerModel.addPropertyChangeListener(this);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("state")) {
            final String viewModelName = (String) evt.getNewValue();
            cardLayout.show(views, viewModelName);

            // Refresh views as needed
            if ("logged in".equals(viewModelName)) {
                for (Component comp : views.getComponents()) {
                    if (comp instanceof LoggedInView) {
                        ((LoggedInView) comp).loadAllListings();
                        break;
                    }
                }
            } else if ("conversations".equals(viewModelName)) {
                for (Component comp : views.getComponents()) {
                    if (comp instanceof ConversationsView) {
                        ((ConversationsView) comp).refreshConversations();
                        break;
                    }
                }
            }
        }
    }
}