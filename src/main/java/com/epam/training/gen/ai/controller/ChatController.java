package com.epam.training.gen.ai.controller;

import com.epam.training.gen.ai.service.SemanticKernelService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatController {

    private final SemanticKernelService semanticKernelService;

    public ChatController(SemanticKernelService semanticKernelService) {
        this.semanticKernelService = semanticKernelService;
    }

    @GetMapping("api/v1/semantic")
    public String getSemanticKernelResponse(@RequestParam(value = "prompt",
            defaultValue = "I want to find top-10 books about world history") String prompt,
                                            @RequestParam(value = "userId", defaultValue = "nobody") String userId) {
        String response = semanticKernelService.handleChatRequest(prompt, userId);
        return response;
    }

}
