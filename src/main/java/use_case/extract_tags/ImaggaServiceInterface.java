package use_case.extract_tags;

import java.util.ArrayList;

public interface ImaggaServiceInterface {
    ArrayList<String> extractTagsFromUrl(String imageUrl) throws Exception;
    ArrayList<String> extractTagsFromFile(String filePath) throws Exception;
}