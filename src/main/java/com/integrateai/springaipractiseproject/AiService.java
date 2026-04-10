package com.integrateai.springaipractiseproject;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

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
        UserTransaction userTransaction = userTransactionRepository.findTopByUsernameOrderByTransactionDateDesc(username);
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

    public String askRecentTransactions(String username, String question) {
        //Get last 5 transactions
        List<UserTransaction> recentTransList = userTransactionRepository.findTop5ByUsernameOrderByTransactionDateDesc(username);

        //Build structured context
        StringBuilder context = new StringBuilder();
        context.append("User: ").append(username).append("\n");
        context.append("Recent Transactions:\n");

        for (UserTransaction tran : recentTransList){
            context.append("_ ")
                    .append(tran.getTransaction())
                    .append(" on")
                    .append(tran.getTransactionDate())
                    .append("\n");
        }

        //Prompt Engineering
        String prompt = """
                You are a banking assistant.
                Use ONLY the data provided below.
                Do NOT make up information.

                %s

                Answer the user question clearly:
                %s
                """.formatted(context.toString(), question);

        //Call AI
        return chatClient.prompt()
                .user(prompt)
                .call()
                .content();

    }

    public Map<String, Double> getSpendingSummary(String username){
        List<UserTransaction> userTransactions = userTransactionRepository.findByUsername(username);
        return userTransactions.stream()
                .collect(Collectors.groupingBy(
                        UserTransaction::getCategory,
                        Collectors.summingDouble(UserTransaction::getAmount)
                ));
    }

    public String askSpendSummary(String username, String question) {
        Map<String, Double> summary = getSpendingSummary(username);
        String highestCategory = Collections.max(summary.entrySet(), Map.Entry.comparingByValue()).getKey();
        String context = "User spending summary: " + summary + ". Explain where the user spends most money in a friendly way."
                + question + "Highest spending category is " + highestCategory;

        return generateAiResponse(context);
    }
}
