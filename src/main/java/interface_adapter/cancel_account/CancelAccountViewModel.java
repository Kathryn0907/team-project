package interface_adapter.cancel_account;

import interface_adapter.ViewModel;

public class CancelAccountViewModel extends ViewModel<CancelAccountState> {

    public CancelAccountViewModel() {
        super("cancel account");
        setState(new CancelAccountState());
    }

}