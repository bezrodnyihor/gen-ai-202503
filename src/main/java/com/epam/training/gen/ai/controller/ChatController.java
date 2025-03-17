package com.epam.training.gen.ai.controller;

import com.epam.training.gen.ai.service.SemanticKernelService;
import com.epam.training.gen.ai.service.SimplePromptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@Slf4j
public class ChatController {

    private final SimplePromptService simplePromptService;

    private final SemanticKernelService semanticKernelService;

    public ChatController(SimplePromptService simplePromptService, SemanticKernelService semanticKernelService) {
        this.simplePromptService = simplePromptService;
        this.semanticKernelService = semanticKernelService;
    }

    @GetMapping("/chat")
    public Map<String, String> getChatResponse(@RequestParam(value = "prompt",
            defaultValue = "I want to find top-10 books about world history") String prompt) {
        Map<String, String> response =simplePromptService.getChatCompletions(prompt);
        log.info("SimplePromptService: {}", response);
        return response;
    }

//    @GetMapping("/semantic")
//    public String getSemanticKernelResponse(@RequestParam(value = "prompt",
//            defaultValue = "I want to find top-10 books about world history") String prompt) {
//        String response = semanticKernelService.getSemanticKernelResponse(prompt);
//        log.info("SemanticKernelService: {}", response);
//        return response;
//    }

}
