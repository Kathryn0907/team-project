package view;

import interface_adapter.ViewManagerModel;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * The View Manager for the program. It listens for property change events
 * in the ViewManagerModel and updates which View should be visible.
 * Updated with Conversations view support and ProfileView auto-refresh.
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

            // Special handling for conversations view
            if ("conversations".equals(viewModelName)) {
                boolean conversationsViewExists = false;
                for (Component comp : views.getComponents()) {
                    if (comp instanceof ConversationsView) {
                        conversationsViewExists = true;
                        ((ConversationsView) comp).refreshConversations();
                        break;
                    }
                }

                if (!conversationsViewExists) {
                    System.err.println("⚠️  Conversations view not found. Make sure user is logged in.");
                    return;
                }
            }

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
            } else if ("profile".equals(viewModelName)) {
                // NEW: Auto-load user's listings from MongoDB when switching to profile
                for (Component comp : views.getComponents()) {
                    if (comp instanceof ProfileView) {
                        ((ProfileView) comp).onViewShown();
                        System.out.println("✅ Auto-loading profile listings from MongoDB");
                        break;
                    }
                }
            }
        }
    }
}