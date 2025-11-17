package use_case.filter;

public interface FilterListingsOutputBoundary {

    /**
     * Called when filtering succeeds, with the filtered listings.
     */
    void present(FilterListingsOutputData outputData);

    /**
     * Called when there is a validation or filtering error.
     * (Optional but very useful for UI).
     */
    default void presentError(String errorMessage) {
        // default no-op; presenter can override
    }
}