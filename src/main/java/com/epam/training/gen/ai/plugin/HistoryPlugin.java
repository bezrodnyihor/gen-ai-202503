package com.epam.training.gen.ai.plugin;

import com.microsoft.semantickernel.semanticfunctions.annotations.DefineKernelFunction;
import com.microsoft.semantickernel.semanticfunctions.annotations.KernelFunctionParameter;
import com.microsoft.semantickernel.services.chatcompletion.ChatHistory;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class HistoryPlugin {

    private final Map<String, ChatHistory> usersHistory = new ConcurrentHashMap<>();

    @DefineKernelFunction(name = "fetchChatHistory", description = "Retrieves chat history.")
    public String fetchChatHistory(@KernelFunctionParameter(name = "userId", description = "User id.") String userId) {
        return usersHistory.get(userId).getMessages()
                .stream()
                .map(chatMessageContent -> chatMessageContent.getAuthorRole().name()+" says" + ": " + chatMessageContent.getContent())
                .collect(Collectors.joining("\n"));
    }

    @DefineKernelFunction(name = "addMessage", description = "Appends message to chat history.")
    public void addMessage(
            @KernelFunctionParameter(name = "userMessage", description = "New message.") String message,
            @KernelFunctionParameter(name = "isUserMessage", description = "User message?",
                    type = boolean.class) boolean isUserMessage,
            @KernelFunctionParameter(name = "userId", description = "User id.") String userId) {
        ChatHistory chatHistory = usersHistory.get(userId);

        if (Objects.isNull(chatHistory)) {
            chatHistory = new ChatHistory();
            usersHistory.put(userId, chatHistory);
        }
        if (isUserMessage) {
            chatHistory.addUserMessage(message);
        } else {
            chatHistory.addAssistantMessage(message);
        }
    }
}