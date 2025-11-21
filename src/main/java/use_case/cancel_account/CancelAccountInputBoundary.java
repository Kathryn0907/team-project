package use_case.cancel_account;

public interface CancelAccountInputBoundary {

    void execute(CancelAccountInputData cancelAccountInputData);

    void back();

}