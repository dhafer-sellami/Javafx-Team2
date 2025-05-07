package services;

import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public class ChatBotService {

    private static final String API_KEY = "AIzaSyCB60B-NndZ29FcP8zP-4LeuNgm6ZUCiUw";
    private static final String ENDPOINT = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-pro-latest:generateContent";
    private static final OkHttpClient client = new OkHttpClient();

    public static String getChatResponse(String question) throws IOException {
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");

        String jsonInput = """
        {
          "contents": [
            {
              "parts": [
                {
                  "text": "%s"
                }
              ]
            }
          ]
        }
        """.formatted(question);

        Request request = new Request.Builder()
                .url(ENDPOINT + "?key=" + API_KEY)
                .post(RequestBody.create(jsonInput, mediaType))
                .header("Content-Type", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                return "Erreur API Gemini : " + response.code();
            }

            String json = response.body().string();
            return extractGeminiText(json);
        }
    }

    private static String extractGeminiText(String json) {
        try {
            JSONObject obj = new JSONObject(json);
            JSONArray candidates = obj.getJSONArray("candidates");
            if (candidates.length() == 0) return "Aucune réponse générée.";

            JSONObject content = candidates.getJSONObject(0).getJSONObject("content");
            JSONArray parts = content.getJSONArray("parts");
            if (parts.length() == 0) return "Réponse vide.";

            return parts.getJSONObject(0).getString("text");
        } catch (Exception e) {
            return "Erreur d'analyse JSON : " + e.getMessage();
        }
    }
}
