package interface_adapter.cancel_account;

import use_case.cancel_account.CancelAccountInputBoundary;
import use_case.cancel_account.CancelAccountInputData;
import use_case.cancel_account.CancelAccountOutputBoundary;

public class CancelAccountController {

    private CancelAccountInputBoundary cancelAccountInteractor;

    public CancelAccountController(CancelAccountInputBoundary cancelAccountInteractor) {
        this.cancelAccountInteractor = cancelAccountInteractor;
    }


    public void execute(String username, String password) {
        final CancelAccountInputData cancelAccountInputData = new CancelAccountInputData(username, password);

        cancelAccountInteractor.execute(cancelAccountInputData);
    }

    public void back() {
        cancelAccountInteractor.back();
    }
}