package com.aicourse.geminiConnection;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class geminiConnection {

    @Value("${spring.ai.google.genai.api-key}")
    private  String apiKey;

    public  String getResponse(String prompt) {

        // Build client WITH API KEY
        Client client = Client.builder()
                .apiKey(apiKey)
                .build();

        GenerateContentResponse response =
                client.models.generateContent(
                        "gemini-2.5-flash",  // model name
                        prompt,
                        null);

        return response.text();
    }

}
