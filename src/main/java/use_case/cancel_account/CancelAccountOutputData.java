package use_case.cancel_account;

public class CancelAccountOutputData {

    private String username;

    public CancelAccountOutputData(String username){
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}