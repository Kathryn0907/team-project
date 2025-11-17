package data_access;

import use_case.extract_tags.ImaggaServiceInterface;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class ImaggaAPIService implements ImaggaServiceInterface {
    private final String apiKey;
    private final String apiSecret;
    private static final String IMAGGA_API_URL = "https://api.imagga.com/v2/tags";

    public ImaggaAPIService(String apiKey, String apiSecret) {
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
    }

    @Override
    public ArrayList<String> extractTagsFromUrl(String imageUrl) throws Exception {
        String encodedImageUrl = URLEncoder.encode(imageUrl, StandardCharsets.UTF_8.toString());
        String urlWithParams = IMAGGA_API_URL + "?image_url=" + encodedImageUrl;
        return makeApiCall(urlWithParams);
    }

    @Override
    public ArrayList<String> extractTagsFromFile(String filePath) throws Exception {
        File fileToUpload = new File(filePath);
        if (!fileToUpload.exists()) {
            throw new FileNotFoundException("File not found: " + filePath);
        }

        return uploadAndExtractTags(fileToUpload);
    }

    private ArrayList<String> makeApiCall(String urlWithParams) throws Exception {
        URL urlObject = new URL(urlWithParams);
        HttpURLConnection connection = (HttpURLConnection) urlObject.openConnection();
        connection.setRequestMethod("GET");

        String credentialsToEncode = apiKey + ":" + apiSecret;
        String basicAuth = Base64.getEncoder().encodeToString(credentialsToEncode.getBytes(StandardCharsets.UTF_8));
        connection.setRequestProperty("Authorization", "Basic " + basicAuth);

        int responseCode = connection.getResponseCode();

        if (responseCode != 200) {
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            StringBuilder errorResponse = new StringBuilder();
            String errorLine;
            while ((errorLine = errorReader.readLine()) != null) {
                errorResponse.append(errorLine);
            }
            errorReader.close();
            throw new IOException("Failed to get tags. Response code: " + responseCode + " - " + errorResponse.toString());
        }

        BufferedReader connectionInput = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = connectionInput.readLine()) != null) {
            response.append(line);
        }
        connectionInput.close();

        return parseTagsFromResponse(response.toString());
    }

    private ArrayList<String> uploadAndExtractTags(File fileToUpload) throws Exception {
        String credentialsToEncode = apiKey + ":" + apiSecret;
        String basicAuth = Base64.getEncoder().encodeToString(credentialsToEncode.getBytes(StandardCharsets.UTF_8));

        String crlf = "\r\n";
        String twoHyphens = "--";
        String boundary = "Image Upload";

        URL urlObject = new URL(IMAGGA_API_URL);
        HttpURLConnection connection = (HttpURLConnection) urlObject.openConnection();
        connection.setRequestProperty("Authorization", "Basic " + basicAuth);
        connection.setUseCaches(false);
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Connection", "Keep-Alive");
        connection.setRequestProperty("Cache-Control", "no-cache");
        connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

        DataOutputStream request = new DataOutputStream(connection.getOutputStream());

        request.writeBytes(twoHyphens + boundary + crlf);
        request.writeBytes("Content-Disposition: form-data; name=\"image\";filename=\"" +
                fileToUpload.getName() + "\"" + crlf);
        request.writeBytes(crlf);

        InputStream inputStream = new FileInputStream(fileToUpload);
        int bytesRead;
        byte[] dataBuffer = new byte[1024];
        while ((bytesRead = inputStream.read(dataBuffer)) != -1) {
            request.write(dataBuffer, 0, bytesRead);
        }
        inputStream.close();

        request.writeBytes(crlf);
        request.writeBytes(twoHyphens + boundary + twoHyphens + crlf);
        request.flush();
        request.close();

        int responseCode = connection.getResponseCode();
        if (responseCode != 200) {
            throw new IOException("Failed to upload file. Response code: " + responseCode);
        }

        InputStream responseStream = new BufferedInputStream(connection.getInputStream());
        BufferedReader responseStreamReader = new BufferedReader(new InputStreamReader(responseStream));

        StringBuilder stringBuilder = new StringBuilder();
        String responseLine;
        while ((responseLine = responseStreamReader.readLine()) != null) {
            stringBuilder.append(responseLine).append("\n");
        }
        responseStreamReader.close();
        responseStream.close();
        connection.disconnect();

        String response = stringBuilder.toString();
        return parseTagsFromResponse(response);
    }

    private ArrayList<String> parseTagsFromResponse(String jsonResponse) {
        ArrayList<String> tags = new ArrayList<>();

        try {
            JSONObject json = new JSONObject(jsonResponse);
            JSONObject status = json.getJSONObject("status");
            String statusType = status.getString("type");

            if (!statusType.equals("success")) {
                System.err.println("Imagga API error: " + status.getString("text"));
                return tags;
            }
            JSONArray tagsArray = json.getJSONObject("result").getJSONArray("tags");

            for (int i = 0; i < tagsArray.length(); i++) {
                JSONObject tagObj = tagsArray.getJSONObject(i);
                double confidence = tagObj.getDouble("confidence");

                if (confidence > 30.0) {
                    String tagName = tagObj.getJSONObject("tag").getString("en");
                    tags.add(tagName);
                }
            }
        } catch (Exception e) {
            System.err.println("Error parsing Imagga response: " + e.getMessage());
            e.printStackTrace();
        }

        return tags;
    }
}