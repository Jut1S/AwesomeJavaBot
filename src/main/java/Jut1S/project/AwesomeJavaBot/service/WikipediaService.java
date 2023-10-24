package Jut1S.project.AwesomeJavaBot.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class WikipediaService {

    private static final String BASE_URL = "https://ru.wikipedia.org/w/api.php/";

    public static String search(String query) {
        RestTemplate restTemplate = new RestTemplate();

        String url = BASE_URL + "?action=query&format=json&list=search&utf8=1&srsearch=" + query;

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        String responseBody = response.getBody();


        try {
            JSONObject json = new JSONObject(responseBody);

            JSONObject queryObject = json.getJSONObject("query");
            JSONArray searchResults = queryObject.getJSONArray("search");


            List<String> results = new ArrayList<>();

            for (int i = 0; i < searchResults.length(); i++) {
                JSONObject searchResult = searchResults.getJSONObject(i);
                String title = searchResult.getString("title");


                String encodedTitle = encode(title);
                String urlResult = "https://ru.wikipedia.org/wiki/" + encodedTitle.replace("+", "_");

                results.add(title + " - " + urlResult);
            }

            return String.join("\n\n", results);
        } catch (JSONException e) {
            e.printStackTrace();
            return "An error occurred while processing the request.";
        }
    }

    private static String encode(String title) {
        try {
            return URLEncoder.encode(title, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
    }
}



