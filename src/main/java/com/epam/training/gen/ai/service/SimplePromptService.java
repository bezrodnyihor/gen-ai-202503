package com.epam.training.gen.ai.service;


import com.azure.ai.openai.OpenAIAsyncClient;
import com.azure.ai.openai.models.ChatCompletionsOptions;
import com.azure.ai.openai.models.ChatRequestUserMessage;
import com.epam.training.gen.ai.configuration.PropertyUtility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@Service
public class SimplePromptService {

    private final OpenAIAsyncClient openAIAsyncClient;
    private final PropertyUtility propertyUtility;

    public SimplePromptService(OpenAIAsyncClient aiClientService, PropertyUtility propertyUtility) {
        this.openAIAsyncClient = aiClientService;
        this.propertyUtility = propertyUtility;
    }


    public  Map<String, String> getChatCompletions(String prompt) {
        var completions = openAIAsyncClient
                .getChatCompletions(
                        propertyUtility.getModelName(),
                        new ChatCompletionsOptions(List.of(new ChatRequestUserMessage(prompt))))
                .block();

        var message = completions.getChoices().stream()
                .map(c -> c.getMessage().getContent())
                .findFirst()
                .orElseThrow(RuntimeException::new);
        log.info(message);

        Map<String, String> jsonResponse = new HashMap<>();
        jsonResponse.put("prompt", prompt);
        jsonResponse.put("response", message);

        return jsonResponse;
    }
}


