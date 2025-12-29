package com.example;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;


public class AgentBilling {
    public static void agentBillingFunction(String[] keyWords, String userQuestion, String api_key,Scanner scanner) {
        HashSet<String> billingDocs = new HashSet<>();
        billingDocs.add("docs/billing_files/billing_policy.txt");
        HashSet<String> preparedBillingDocs = new HashSet<>();
        HashMap<String, String> billingData = getBillingData();
        String regex = "\\R\\s*\\R";
        
        preparedBillingDocs = FileContentReader.prepareDocumentation(billingDocs, keyWords, regex);
        
        Client client = Client.builder()
                                .apiKey(api_key)
                                .build();
        String documentationString = ""; 
        for(String i: preparedBillingDocs){
            documentationString += i + "\n---\n";
        }
        String systemPrompt;
        try {
          systemPrompt = FileContentReader.loadPrompt("prompts/tool_prompt.txt");
        } catch (IOException e) {
            System.err.println("Error reading prompt resource");
            e.printStackTrace();
            System.exit(1);
            return;
        }

        String fullPrompt =systemPrompt + userQuestion + "\nRelevant documentation:\n" + documentationString;
        GenerateContentResponse response =
              client.models.generateContent(
                "gemini-2.5-flash",
                fullPrompt,
                null );
        System.out.println(response.text());
        String text = response.text();
            if (text.contains("TOOL_CALL:")) {
                String toolName = text.substring(text.indexOf("TOOL_CALL:") + 10).trim().split("\\R")[0].trim();

                System.out.println("\n[Tool execution required: " + toolName + "]");
                System.out.println("To proceed, I need your User ID.");

                String currentUserId = null;
                while (currentUserId == null) {
                    System.out.print("Please enter your User ID (or type 'cancel' to skip): ");
                    String input = scanner.nextLine().trim();

                    if (input.equalsIgnoreCase("cancel")) {
                        System.out.println("Tool execution cancelled.");
                        return;
                    }

                    if (billingData.containsKey(input)) {
                        currentUserId = input;
                        System.out.println("Verified! Your current plan: " + billingData.get(currentUserId));
                    } else {
                        System.out.println("Invalid User ID. Please try again.");
                    }
                }

            switch (toolName) {
                case "get_current_plan":
                    if (currentUserId == null) {
                        System.out.println("User ID is not provided.");
                        break;
                    }

                    String plan = billingData.get(currentUserId);
                    System.out.println("User current plan  " + currentUserId + " to: " + plan);
                    
                    break;
                case "explain_refund_policy":
                    String refundPolicy = "Refund Policy:\r\n" + //
                                                "\r\n" + //
                                                "• Full refund within 14 days of the first payment (no questions asked).\r\n" + //
                                                "• After 14 days: no refund for the current cycle.\r\n" + //
                                                "• Exception: prorated refund for confirmed service outage >24 hours.\r\n" + //
                                                "• Refund within 5-10 business days.";
                    System.out.println(refundPolicy);
                    break;
                case "create_refund_request":
                    String result = "Link to form:\r\n" + //
                                                "https://example.com/refund?user=%s&token=%s\r\n" + //
                                                "\r\n" + //
                                                "We will process your request within 5-10 business days.";
                    System.out.println(result);
                    break;
                default:
                    System.out.println("Unknown tool: " + toolName);
                    return;
            }
            

            System.out.println("Executed tool" + toolName);
            

        } else {
            // Normalna odpowiedź
            System.out.println("Agent response:");
            System.out.println(text);
        }        

    }
    public static HashMap<String, String> getBillingData() {
        HashMap<String, String> billingData = new HashMap<>();
        
        try {
            String content = FileContentReader.loadPrompt("docs/billing_files/users.txt");
            String[] lines = content.split("\n");

            for (int i = 1; i < lines.length; i++) {
                String line = lines[i].trim();
                if (line.isEmpty() || line.startsWith("#")) continue; 
                
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    String userId = parts[0].trim();
                    String plan = parts[1].trim();
                    billingData.put(userId, plan);
                }
            }
                        
        } catch (Exception e) {
            System.err.println("Error with reciving users.txt.");
        }
        
        return billingData;
    }
}


