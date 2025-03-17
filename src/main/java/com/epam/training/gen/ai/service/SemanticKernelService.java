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
        var response = Objects.requireNonNull(chatCompletionService.getChatMessageContentsAsync(chatHistory, kernel, invocationContext).block())
                .stream()
                .map(ChatMessageContent::getContent)
                .findFirst()
                .orElseThrow(RuntimeException::new);
        chatHistory.addUserMessage(prompt);
        return response;
    }

}
