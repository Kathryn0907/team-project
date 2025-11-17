package use_case.check_favorite;

/**
 * Output Boundary for the Check Favorite Use Case.
 */
public interface CheckFavoriteOutputBoundary {
    /**
     * Prepares the success view for the Check Favorite Use Case.
     * @param outputData the output data
     */
    void prepareSuccessView(CheckFavoriteOutputData outputData);

    /**
     * Prepares the failure view for the Check Favorite Use Case.
     * @param errorMessage the explanation of the failure
     */
    void prepareFailView(String errorMessage);
}