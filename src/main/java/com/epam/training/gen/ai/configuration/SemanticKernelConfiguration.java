package com.epam.training.gen.ai.configuration;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.core.credential.AzureKeyCredential;
import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.aiservices.openai.chatcompletion.OpenAIChatCompletion;
import com.microsoft.semantickernel.aiservices.openai.textcompletion.OpenAITextGenerationService;
import com.microsoft.semantickernel.orchestration.InvocationContext;
import com.microsoft.semantickernel.orchestration.InvocationReturnMode;
import com.microsoft.semantickernel.orchestration.ToolCallBehavior;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;
import com.microsoft.semantickernel.services.textcompletion.TextGenerationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class SemanticKernelConfiguration {

    private final PropertyUtility propertyUtility;

    public SemanticKernelConfiguration(PropertyUtility propertyUtility) {
        this.propertyUtility = propertyUtility;
    }

    @Bean
    public OpenAIAsyncClient openAIAsyncClient() {
        return new OpenAIClientBuilder()
                .credential(new AzureKeyCredential(propertyUtility.getApiKey()))
                .endpoint(propertyUtility.getApiEndpoint())
                .buildAsyncClient();
    }

    @Bean
    public Kernel kernel(OpenAIAsyncClient openAIAsyncClient){
        return Kernel.builder()
                .withAIService(TextGenerationService.class,
                        OpenAITextGenerationService.builder()
                                .withModelId(propertyUtility.getModelName())
                                .withOpenAIAsyncClient(openAIAsyncClient)
                                .build())
                .build();
    }

    @Bean
    public InvocationContext invocationContext() {
        return InvocationContext.builder()
                .withReturnMode(InvocationReturnMode.LAST_MESSAGE_ONLY)
                .withToolCallBehavior(ToolCallBehavior.allowAllKernelFunctions(true))
                .build();
    }

    @Bean
    public ChatCompletionService chatCompletionService(OpenAIAsyncClient openAIAsyncClient) {
        return OpenAIChatCompletion.builder()
                .withModelId(propertyUtility.getModelName())
                .withOpenAIAsyncClient(openAIAsyncClient)
                .build();
    }

}
