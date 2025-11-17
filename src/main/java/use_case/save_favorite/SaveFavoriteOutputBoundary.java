package use_case.save_favorite;

/**
 * Output Boundary for the Save Favorite Use Case.
 */
public interface SaveFavoriteOutputBoundary {
    /**
     * Prepares the success view for the Save Favorite Use Case.
     * @param outputData the output data
     */
    void prepareSuccessView(SaveFavoriteOutputData outputData);

    /**
     * Prepares the failure view for the Save Favorite Use Case.
     * @param errorMessage the explanation of the failure
     */
    void prepareFailView(String errorMessage);
}