package com.integrateai.springaipractiseproject;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AiController {

    private final AiService aiService;
    private final JwtUtility jwtUtility = new JwtUtility();
    private final AppUserRepository appUserRepository;

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

    @PostMapping("auth/login")
    public String login(@RequestBody LoginRequest request){
        AppUser user = appUserRepository.findByUsername(request.getUsername()).orElseThrow();
        if (!user.getPassword().equalsIgnoreCase(request.getPassword())){
            throw new IllegalArgumentException("Login Failed");
        }
        return jwtUtility.generateToken(request.getUsername());
    }

    @PostMapping("register")
    public AppUser register(@RequestBody LoginRequest request){
        return aiService.registerUser(request);
    }

    @GetMapping("ai/transaction2")
    public String askTransactionWithAuth(@RequestParam String question, HttpServletRequest request){
        String username = (String) request.getAttribute("username");
        return aiService.askAboutTransaction(question, username);
    }

    @PostMapping("do/transaction")
    public UserTransaction createTransaction(@RequestBody UserTransaction request, HttpServletRequest authRequest){
        String username = (String) authRequest.getAttribute("username");
        return aiService.createTransaction(request, username);
    }
}
