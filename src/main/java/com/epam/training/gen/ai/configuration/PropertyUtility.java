package com.epam.training.gen.ai.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
final public class PropertyUtility {
    @Value("${client-azureopenai-key}")
    private String apiKey;

    @Value("${client-azureopenai-endpoint}")
    private String apiEndpoint;

    @Value("${client-azureopenai-deployment-name}")
    private String modelName;
}
