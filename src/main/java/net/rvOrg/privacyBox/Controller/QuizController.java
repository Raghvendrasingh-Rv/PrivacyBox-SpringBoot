package net.rvOrg.privacyBox.Controller;

import net.rvOrg.privacyBox.Entity.OpenAIRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/quiz")
public class QuizController {

    @Value("${openrouter.api.key}")
    private String apiKey;

    private final String OPENROUTER_URL = "https://openrouter.ai/api/v1/chat/completions";

    @PostMapping
    public String generateQuiz(@RequestBody Map<String, String> requestBody) {
        String noteText = requestBody.get("text");

        OpenAIRequest request = new OpenAIRequest(List.of(
                new OpenAIRequest.Message("system", "You are a quiz generator AI."),
                new OpenAIRequest.Message("user", "Generate 5 MCQs from this content: " + noteText)
        ));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiKey);


        HttpEntity<OpenAIRequest> entity = new HttpEntity<>(request, headers);
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<Map> response = restTemplate.exchange(
                OPENROUTER_URL, HttpMethod.POST, entity, Map.class
        );

        List<Map<String, Object>> choices = (List<Map<String, Object>>) response.getBody().get("choices");
        Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
        return message.get("content").toString();
    }
}
