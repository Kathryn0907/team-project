package use_case.filter;

public interface FilterListingsInputBoundary {

    /**
     * Run the Filter Listings use case with the given input data.
     */
    void execute(FilterListingsInputData inputData);
}