package use_case.signup;


/**
 * Output Boundary for actions which are related to signing up.
 */
public interface SignupOutputBoundary {


    /**
     * Executes the signup use case.
     * @param signupOutputData the output data
     */
    void prepareSuccess(SignupOutputData signupOutputData);



    void prepareFailure(String message);


    void switchToLogin();
}
