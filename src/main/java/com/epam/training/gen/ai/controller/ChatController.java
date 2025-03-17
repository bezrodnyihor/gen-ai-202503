package com.epam.training.gen.ai.controller;

import com.epam.training.gen.ai.service.SemanticKernelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class ChatController {

    private final SemanticKernelService semanticKernelService;

    public ChatController(SemanticKernelService semanticKernelService) {
        this.semanticKernelService = semanticKernelService;
    }

    @GetMapping("/semantic")
    public String getSemanticKernelResponse(@RequestParam(value = "prompt",
            defaultValue = "I want to find top-10 books about world history") String prompt) {
        String response = semanticKernelService.getSemanticKernelResponse(prompt);
        log.info("SemanticKernelService: {}", response);
        return response;
    }

}
