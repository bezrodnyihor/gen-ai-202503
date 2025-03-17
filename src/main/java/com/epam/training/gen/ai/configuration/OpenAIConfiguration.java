package com.epam.training.gen.ai.configuration;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.microsoft.semantickernel.connectors.ai.openai.util.ClientType;
import com.microsoft.semantickernel.connectors.ai.openai.util.OpenAIClientProvider;
import com.microsoft.semantickernel.exceptions.ConfigurationException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.Map;

@Configuration
public class OpenAIConfiguration {

    private final PropertyUtility propertyUtility;

    public OpenAIConfiguration(PropertyUtility propertyUtility) {
        this.propertyUtility = propertyUtility;
    }

    @Primary
    @Bean
    public OpenAIAsyncClient openAIAsyncClient() throws ConfigurationException {
        var configuredSettings = Map.of(
                "client.azureopenai.key", propertyUtility.getApiKey(),
                "client.azureopenai.endpoint", propertyUtility.getApiEndpoint(),
                "client.azureopenai.deploymentname", propertyUtility.getModelName());
        OpenAIClientProvider provider = new OpenAIClientProvider(configuredSettings, ClientType.AZURE_OPEN_AI);
        return provider.getAsyncClient();
    }
}
