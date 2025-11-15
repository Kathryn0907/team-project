package use_case.extract_tags;

import java.util.ArrayList;

public class ExtractTagsOutputData {
    private final ArrayList<String> extractedTags;
    private final ArrayList<String> categories;
    private final boolean success;

    public ExtractTagsOutputData(ArrayList<String> extractedTags, ArrayList<String> categories, boolean success) {
        this.extractedTags = extractedTags;
        this.categories = categories;
        this.success = success;
    }

    public ArrayList<String> getExtractedTags() {
        return extractedTags;
    }

    public ArrayList<String> getCategories() {
        return categories;
    }

    public boolean isSuccess() {
        return success;
    }
}