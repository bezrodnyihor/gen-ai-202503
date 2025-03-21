package com.epam.training.gen.ai.service;

import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.orchestration.FunctionResult;
import com.microsoft.semantickernel.orchestration.PromptExecutionSettings;
import com.microsoft.semantickernel.semanticfunctions.KernelFunction;
import com.microsoft.semantickernel.semanticfunctions.KernelFunctionArguments;
import com.microsoft.semantickernel.services.chatcompletion.ChatHistory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class SemanticKernelService {
    private final Kernel kernel;

    private final Map<String, ChatHistory> chatHistoryMap = new ConcurrentHashMap<>();
    private final Map<String, PromptExecutionSettings> promptExecutionsSettingsMap;

    private static final String SYSTEM_PROMPT = """
                You are an information assistant providing concise and short answers.
            """;

    public SemanticKernelService(Kernel kernel, Map<String, PromptExecutionSettings> promptExecutionsSettingsMap) {
        this.kernel = kernel;
        this.promptExecutionsSettingsMap = promptExecutionsSettingsMap;
    }

    public String handleChatRequest(String prompt, String userId) {
        ChatHistory chatHistory = chatHistoryMap.computeIfAbsent(userId, key -> new ChatHistory(SYSTEM_PROMPT));

        FunctionResult<String> response = kernel.invokeAsync(retrieveChatHistory())
                .withArguments(getKernelFunctionArguments(prompt, chatHistory))
                .block();
        if(Objects.isNull(response)){
            return "Sorry, I can't answer this question";
        }

        chatHistory.addUserMessage(prompt);
        chatHistory.addAssistantMessage(response.getResult());
        log.info("response: {}", response.getResult());
        return response.getResult();
    }
    private KernelFunction<String> retrieveChatHistory() {
        return KernelFunction.<String>createFromPrompt("""
                        {{$chatHistory}}
                        <message role="user">{{$request}}</message>""")
                .withExecutionSettings(promptExecutionsSettingsMap)
                .build();
    }

    private KernelFunctionArguments getKernelFunctionArguments(String prompt, ChatHistory chatHistory) {
        return KernelFunctionArguments.builder()
                .withVariable("request", prompt)
                .withVariable("chatHistory", chatHistory)
                .build();
    }
}