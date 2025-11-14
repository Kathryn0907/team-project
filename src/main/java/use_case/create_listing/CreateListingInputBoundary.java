package use_case.create_listing;

/**
 * Input Boundary for actions which are related to signing up
 */
public interface CreateListingInputBoundary {
    /**
     * Executes the create_listing use case
     * @param createListingInputData the input data
     */
    void execute(CreateListingInputData createListingInputData);

    void swtichToProfileView();
}
