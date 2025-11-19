package use_case.cancel_account;

public interface CancelAccountOutputBoundary {

    void prepareSuccess(CancelAccountOutputData cancelAccountOutputData);

    void prepareFailure(String message);

    void back();
}