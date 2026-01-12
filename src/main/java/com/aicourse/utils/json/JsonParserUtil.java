package com.aicourse.utils.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonParserUtil {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private JsonParserUtil() {

    }

    /**
     * Converts a JSON-formatted String into a JsonNode (JSONObject equivalent).
     *
     * @param jsonString JSON in String format
     * @return JsonNode (JSONObject)
     * @throws IllegalArgumentException if input is null or invalid JSON
     */
    public static JsonNode parseStringToJsonObject(String jsonString) {
        if (jsonString == null || jsonString.trim().isEmpty()) {
            throw new IllegalArgumentException("JSON string cannot be null or empty");
        }

        try {
            return OBJECT_MAPPER.readTree(jsonString);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid JSON string", e);
        }
    }
    public static String extractRawJson(String aiResponse) {
        if (aiResponse == null || aiResponse.trim().isEmpty()) {
            throw new IllegalArgumentException("AI response is empty");
        }

        String cleaned = aiResponse.trim();

        if (cleaned.startsWith("```")) {
            cleaned = cleaned.replaceAll("^```[a-zA-Z]*", "");
            cleaned = cleaned.replaceAll("```$", "");
        }

        return cleaned.trim();
    }
}
