package use_case.cancel_account;

import Entities.User;

public class CancelAccountInteractor implements CancelAccountInputBoundary {

    private final CancelAccountDataAccessInterface dataAccess;
    private final CancelAccountOutputBoundary cancelAccountPresenter;


    public CancelAccountInteractor(CancelAccountDataAccessInterface dataAccess, CancelAccountOutputBoundary outputPresenter) {
        this.dataAccess = dataAccess;
        this.cancelAccountPresenter = outputPresenter;
    }


    @Override
    public void execute(CancelAccountInputData cancelAccountInputData) {
        final String username = cancelAccountInputData.getUsername();
        final String password = cancelAccountInputData.getPassword();

        if (!dataAccess.existByUsername(username)) {
            cancelAccountPresenter.prepareFailure("User not found");
        }

        else {
            final User user = dataAccess.getUser(username);
            if (!password.equals(user.getPassword())) {
                cancelAccountPresenter.prepareFailure("Wrong password");
            } else {

                dataAccess.cancelAccount(username);

                final CancelAccountOutputData cancelAccountOutputData = new CancelAccountOutputData(username);
                cancelAccountPresenter.prepareSuccess(cancelAccountOutputData);
            }
        }
    }

    @Override
    public void back() {
        cancelAccountPresenter.back();
    }
}