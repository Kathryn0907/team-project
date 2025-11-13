package use_case.signup;


/**
 * Output data for signup use case
 */

public class SignupOutputData {


    private String username;

    public SignupOutputData(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
