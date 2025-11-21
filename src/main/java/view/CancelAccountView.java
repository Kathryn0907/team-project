package view;

import interface_adapter.cancel_account.CancelAccountController;
import interface_adapter.cancel_account.CancelAccountState;
import interface_adapter.cancel_account.CancelAccountViewModel;
import interface_adapter.login.LoginState;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class CancelAccountView extends JPanel implements ActionListener, PropertyChangeListener {

    private final String viewName = "cancel account";
    private final CancelAccountViewModel cancelAccountViewModel;

    private final JTextField usernameInputField = new JTextField(15);
    private final JLabel usernameErrorField = new JLabel();

    private final JPasswordField passwordInputField = new JPasswordField(15);
    private final JLabel passwordErrorField = new JLabel();

    private final JButton confirmButton;
    private final JButton backButton;

    private CancelAccountController cancelAccountController = null;


    public CancelAccountView(CancelAccountViewModel cancelAccountViewModel) {

        this.cancelAccountViewModel = cancelAccountViewModel;
        this.cancelAccountViewModel.addPropertyChangeListener(this);

        final JLabel title = new JLabel("Cancel Account");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        final LabelTextPanel usernameInfo = new LabelTextPanel(
                new JLabel("Username"), usernameInputField);
        final LabelTextPanel passwordInfo = new LabelTextPanel(
                new JLabel("Password"), passwordInputField);

        final JPanel buttons = new JPanel();
        confirmButton = new JButton("Confirm cancel");
        buttons.add(confirmButton);
        backButton = new JButton("Back to Profile");
        buttons.add(backButton);

        confirmButton.addActionListener(this);

        backButton.addActionListener(new ActionListener() {
                                        public void actionPerformed(ActionEvent e) {
                                            cancelAccountController.back();
                                        }
                                    }
        );

        usernameInputField.getDocument().addDocumentListener(new DocumentListener() {

            private void documentListenerHelper() {
                final CancelAccountState currentState = cancelAccountViewModel.getState();
                currentState.setUsername(usernameInputField.getText());
                cancelAccountViewModel.setState(currentState);
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                documentListenerHelper();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                documentListenerHelper();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                documentListenerHelper();
            }
        });

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        passwordInputField.getDocument().addDocumentListener(new DocumentListener() {

            private void documentListenerHelper() {
                final CancelAccountState currentState = cancelAccountViewModel.getState();
                currentState.setPassword(new String(passwordInputField.getPassword()));
                cancelAccountViewModel.setState(currentState);
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                documentListenerHelper();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                documentListenerHelper();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                documentListenerHelper();
            }
        });

        this.add(title);
        this.add(usernameInfo);
        this.add(usernameErrorField);
        this.add(passwordInfo);
        this.add(passwordErrorField);
        this.add(buttons);
    }




    @Override
    public void actionPerformed(ActionEvent evt) {
        if (evt.getSource().equals(confirmButton)) {
            final CancelAccountState currentState = cancelAccountViewModel.getState();

            cancelAccountController.execute(
                    currentState.getUsername(),
                    currentState.getPassword()
            );
        }
    }


    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        final CancelAccountState state = (CancelAccountState) evt.getNewValue();
        setFields(state);
        usernameErrorField.setText(state.getCancelAccountError());
    }


    private void setFields(CancelAccountState state) {
        usernameInputField.setText(state.getUsername());
    }


    public String getViewName() {
        return viewName;
    }

    public void setCancelAccountController(CancelAccountController cancelAccountController) {
        this.cancelAccountController = cancelAccountController;
    }


}
