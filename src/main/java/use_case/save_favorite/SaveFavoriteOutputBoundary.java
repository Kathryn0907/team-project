package use_case.save_favorite
        
public interface SaveFavoriteOutputBoundary {
    void prepareSuccessView(SaveFavoriteOutputBoundary outputData);
    void prepareFailView(String errorMessage);
}