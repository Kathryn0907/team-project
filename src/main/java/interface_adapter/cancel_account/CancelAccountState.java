package interface_adapter.cancel_account;

public class CancelAccountState {

    private String username = "";
    private String cancelAccountError;
    private String password = "";

    public String getUsername() {return username;}

    public String getCancelAccountError() {return cancelAccountError;}

    public String getPassword() {return password;}

    public void setUsername(String username) {this.username = username;}

    public void setCancelAccountError(String cancelAccountError) {
        this.cancelAccountError = cancelAccountError;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}