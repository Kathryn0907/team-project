package use_case.check_favorite;

/**
 * Input Boundary for the Check Favorite Use Case.
 */
public interface CheckFavoriteInputBoundary {
    /**
     * Executes the check favorite use case.
     * @param inputData the input data
     */
    void execute(CheckFavoriteInputData inputData);
}