package com.epam.training.gen.ai.service;


import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.orchestration.InvocationContext;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;
import com.microsoft.semantickernel.services.chatcompletion.ChatHistory;
import com.microsoft.semantickernel.services.chatcompletion.ChatMessageContent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Slf4j
public class SemanticKernelService {
    private final Kernel kernel;
    private final InvocationContext invocationContext;
    private final ChatCompletionService chatCompletionService;

    public SemanticKernelService(Kernel kernel, InvocationContext invocationContext, ChatCompletionService chatCompletionService) {
        this.kernel = kernel;
        this.invocationContext = invocationContext;
        this.chatCompletionService = chatCompletionService;
    }


    public String getSemanticKernelResponse(String prompt) {
        var chatHistory = new ChatHistory();
        chatHistory.addUserMessage(prompt);
        String response = Objects.requireNonNull(chatCompletionService.getChatMessageContentsAsync(chatHistory, kernel, invocationContext).block())
                .stream()
                .findFirst()
                .map(ChatMessageContent::getContent)
                .orElse("Sorry, I don't know the answer to this question");
        chatHistory.addUserMessage(prompt);
        return response;
    }

}