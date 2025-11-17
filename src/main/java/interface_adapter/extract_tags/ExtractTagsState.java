package interface_adapter.extract_tags;

import java.util.ArrayList;
import java.util.List;

public class ExtractTagsState {
    private List<String> extractedTags = new ArrayList<>();
    private List<String> categories = new ArrayList<>();
    private String errorMessage = "";
    private boolean success = false;

    public List<String> getExtractedTags() {
        return extractedTags;
    }

    public void setExtractedTags(List<String> extractedTags) {
        this.extractedTags = extractedTags;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}