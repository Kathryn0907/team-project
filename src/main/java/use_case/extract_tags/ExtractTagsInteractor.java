package use_case.extract_tags;

import Entities.Listing;
import java.util.*;

public class ExtractTagsInteractor implements ExtractTagsInputBoundary {
    private final ExtractTagsDataAccessInterface dataAccess;
    private final ImaggaServiceInterface imaggaService;
    private final ExtractTagsOutputBoundary presenter;

    public ExtractTagsInteractor(ExtractTagsDataAccessInterface dataAccess,
                                 ImaggaServiceInterface imaggaService,
                                 ExtractTagsOutputBoundary presenter) {
        this.dataAccess = dataAccess;
        this.imaggaService = imaggaService;
        this.presenter = presenter;
    }

    @Override
    public ExtractTagsOutputData execute(ExtractTagsInputData inputData) {
        try {
            Listing listing = dataAccess.getListingByName(inputData.getListingName());
            if (listing == null) {
                presenter.prepareFailView("Listing not found");
                return new ExtractTagsOutputData(new ArrayList<>(), new ArrayList<>(), false);
            }

            ArrayList<String> extractedTags = imaggaService.extractTagsFromUrl(inputData.getImageUrl());
            for (String tag : extractedTags) {
                listing.addTag(tag);
            }

            ArrayList<String> categories = categorizeListing(extractedTags);
            for (String category : categories) {
                listing.addCategory(category);
            }
            dataAccess.saveListing(listing);
            ExtractTagsOutputData outputData = new ExtractTagsOutputData(extractedTags, categories, true);
            presenter.prepareSuccessView(outputData);
            return outputData;

        } catch (Exception e) {
            presenter.prepareFailView("Failed to extract tags: " + e.getMessage());
            return new ExtractTagsOutputData(new ArrayList<>(), new ArrayList<>(), false);
        }
    }

    private ArrayList<String> categorizeListing(ArrayList<String> imageTags) {
        ArrayList<String> categories = new ArrayList<>();

        HashMap<String, ArrayList<String>> categoryKeywords = new HashMap<>();
        categoryKeywords.put("apartment", new ArrayList<>(Arrays.asList("apartment", "flat", "unit", "condominium", "studio")));
        categoryKeywords.put("house", new ArrayList<>(Arrays.asList("house", "home", "residence", "bungalow", "mansion")));
        categoryKeywords.put("villa", new ArrayList<>(Arrays.asList("villa", "estate", "manor")));
        categoryKeywords.put("cottage", new ArrayList<>(Arrays.asList("cottage", "cabin", "chalet", "lodge")));
        categoryKeywords.put("condo", new ArrayList<>(Arrays.asList("condo", "condominium")));

        for (String tag : imageTags) {
            String lowerTag = tag.toLowerCase();
            for (Map.Entry<String, ArrayList<String>> entry : categoryKeywords.entrySet()) {
                for (String keyword : entry.getValue()) {
                    if (lowerTag.contains(keyword) && !categories.contains(entry.getKey())) {
                        categories.add(entry.getKey());
                    }
                }
            }
        }

        if (categories.isEmpty()) {
            categories.add("property");
        }

        return categories;
    }
}