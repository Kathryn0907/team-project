package use_case.signup;

import Entities.User;


/**
 * signup interactor
 */
public class SignupInteractor implements SignupInputBoundary{

    private SignupOutputBoundary signupPresenter;
    private SignupDataAccessInterface signupDataAccess;



    public SignupInteractor(SignupOutputBoundary signupPresenter,
                            SignupDataAccessInterface signupDataAccess) {
        this.signupPresenter = signupPresenter;
        this.signupDataAccess = signupDataAccess;
    }


    @Override
    public void execute(SignupInputData signupInputData) {
        if(signupDataAccess.existUsername(signupInputData.getUsername())){
            signupPresenter.prepareFailure("Username is already exist");
        } else if(!signupInputData.getPassword().equals(signupInputData.getPassword())){
            signupPresenter.prepareFailure("Passwords do not match");
        } else if("".equals(signupInputData.getUsername())){
            signupPresenter.prepareFailure("Username is empty");
        } else if("".equals(signupInputData.getPassword())){
            signupPresenter.prepareFailure("Password is empty");
        } else if(signupInputData.getPassword().length() < 6){
            signupPresenter.prepareFailure("Password length is less than 6 characters");
        }
        else {
            // wait for User class
            /*
            final User signupUser = new User(...);
            signupDataAccess.save(signupUser);

            final SignupOutputData signupOutputData =
                    new SignupOutputData(signupUser.getusername());
            signupPresenter.prepareSuccess(signupOutputData);
             */
        }
    }

    @Override
    public void switchToLogin(){
        signupPresenter.switchToLogin();
    }

}

