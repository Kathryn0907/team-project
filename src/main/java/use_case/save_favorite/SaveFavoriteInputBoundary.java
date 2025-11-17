package use_case.save_favorite;

/**
 * Input Boundary for the Save Favorite Use Case.
 */
public interface SaveFavoriteInputBoundary {
    /**
     * Executes the save favorite use case.
     * @param inputData the input data
     */
    void execute(SaveFavoriteInputData inputData);
}