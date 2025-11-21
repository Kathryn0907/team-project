package use_case.cancel_account;

public class CancelAccountInputData {

    private final String username;
    private final String password;

    public CancelAccountInputData(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}