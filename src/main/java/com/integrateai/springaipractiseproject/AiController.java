package com.integrateai.springaipractiseproject;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AiController {

    private final AiService aiService;

    @GetMapping("ai")
    public String generation(@RequestParam String userInput){
        return aiService.generateAiResponse(userInput);
    }

    @GetMapping("ai/transaction")
    public String askTransaction(@RequestParam String username, @RequestParam String question){
        return aiService.askAboutTransaction(question, username);
    }

    @GetMapping("ai/recent-transactions")
    public String askRecentTransactions(@RequestParam String username, @RequestParam String question){
        return aiService.askRecentTransactions(username, question);
    }

    @GetMapping("ai/spend-summary")
    public String askSpendSummary(@RequestParam String username, @RequestParam String question){
        return aiService.askSpendSummary(username, question);
    }
}
