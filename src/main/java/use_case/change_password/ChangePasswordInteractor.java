package use_case.change_password;

import Entities.User;

/**
 * The Change Password Interactor.
 */
public class ChangePasswordInteractor implements ChangePasswordInputBoundary {
    private final ChangePasswordUserDataAccessInterface userDataAccessObject;
    private final ChangePasswordOutputBoundary userPresenter;

    public ChangePasswordInteractor(ChangePasswordUserDataAccessInterface changePasswordDataAccessInterface,
                                    ChangePasswordOutputBoundary changePasswordOutputBoundary) {
        this.userDataAccessObject = changePasswordDataAccessInterface;
        this.userPresenter = changePasswordOutputBoundary;
    }

    @Override
    public void execute(ChangePasswordInputData changePasswordInputData) {
        if ("".equals(changePasswordInputData.getPassword())) {
            userPresenter.prepareFailView("New password cannot be empty");
        }
        else {
            final User user = new User(changePasswordInputData.getUsername(),
                    changePasswordInputData.getPassword());

            userDataAccessObject.changePassword(user);

            final ChangePasswordOutputData changePasswordOutputData = new ChangePasswordOutputData(user.getUsername());
            userPresenter.prepareSuccessView(changePasswordOutputData);
        }
    }
}
