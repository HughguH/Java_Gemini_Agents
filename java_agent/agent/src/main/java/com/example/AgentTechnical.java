package com.example;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import java.util.HashSet;
import java.util.Scanner;

public class AgentTechnical {
    public static void agentTechnicalFunction(String[] keyWords, String userQuestion, String api_key,Scanner scanner){
        HashSet<String> technicalDocs = new HashSet<>();
        technicalDocs.add("docs/technical_files/api_integration_guide.txt");
        technicalDocs.add("docs/technical_files/database_connection_issues.txt");
        technicalDocs.add("docs/technical_files/login_throubleshot.txt");
        technicalDocs.add("docs/technical_files/performance_optimization.txt");

        HashSet<String> preparedTechnicalDocs = new HashSet<>();
        String regex = "\\R\\s*\\R";


        
        preparedTechnicalDocs = FileContentReader.prepareDocumentation(technicalDocs, keyWords, regex);

        
        Client client = Client.builder()
                                .apiKey(api_key)
                                .build();
        String documentationString = ""; 
        for(String i: preparedTechnicalDocs){
            documentationString += i + "\n---\n";
        }
        String fullPrompt ="User question: " + userQuestion + "\nRelevant documentation:\n" + documentationString;
        GenerateContentResponse response =
              client.models.generateContent(
                "gemini-2.5-flash",
                fullPrompt,
                null );
        System.out.println(response.text());
        

    }
}
