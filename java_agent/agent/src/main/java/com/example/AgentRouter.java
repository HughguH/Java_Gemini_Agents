package com.example;
import java.util.Arrays;
import java.util.Scanner;

public class AgentRouter {
    public void agentRouter(String[] preparedDecision, String userQuestion, String api_key,Scanner scanner) {
        // Placeholder for AgentRouter logic
        String[] keyWords = Arrays.copyOfRange(preparedDecision, 1, preparedDecision.length);
        if (preparedDecision[0].equals("Technical")) {
            System.out.println("Routing to Technical Agent...");
            // Call technical agent logic here
            AgentTechnical.agentTechnicalFunction(keyWords, userQuestion, api_key,scanner);
        } else if (preparedDecision[0].equals("Billing")) {
            System.out.println("Routing to Billing Agent...");
            // Call billing agent logic here
            AgentBilling.agentBillingFunction(keyWords, userQuestion, api_key,scanner);
        } 
    }
}
