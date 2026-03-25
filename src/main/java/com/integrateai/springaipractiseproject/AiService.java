package com.integrateai.springaipractiseproject;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class AiService {

    private final ChatClient chatClient;
    private final UserTransactionRepository userTransactionRepository;

    public AiService(ChatClient.Builder chatClientBuilder, UserTransactionRepository userTransactionRepository){
        this.chatClient = chatClientBuilder.build();
        this.userTransactionRepository = userTransactionRepository;
    }

    public String generateAiResponse(String userPrompt){
        return chatClient.prompt()
                .user(userPrompt)
                .call()
                .content();
    }

    public String askAboutTransaction(String userPrompt, String username){
        UserTransaction userTransaction = userTransactionRepository.findByUsername(username);
        if (Objects.isNull(userTransaction)){
            return "No account found for username: " + username;
        }
        String context = "Username: " + userTransaction.getUsername() +
                ", Balance: " + userTransaction.getBalance() + ".";
        String prompt = "You are a helpful banking assistant.\n" + context + "\nUser asks: " + userPrompt;

        return chatClient.prompt()
                .user(prompt)
                .call()
                .content();
    }
}
