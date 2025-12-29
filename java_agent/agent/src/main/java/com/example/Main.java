package com.example;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;

import java.io.IOException;
import java.util.Scanner;



public class Main {

    public static void main(String[] args) {

      
      
      String api_key = System.getenv("GOOGLE_API_KEY");
      if (api_key == null || api_key.isEmpty()) {
        System.err.println("Error: The environment variable GOOGLE_API_KEY is not set.");
        return;
      }

      String promptContent ;
      try {
          promptContent = FileContentReader.loadPrompt("prompts/decision_prompt.txt");
      } catch (IOException e) {
          System.err.println("Error reading prompt resource");
          e.printStackTrace();
          System.exit(1);
          return;
      }
      Client client = null;
      System.out.println("Write your question:");
      Scanner scanner = new Scanner(System.in);
      AgentRouter router = new AgentRouter();
      String[] preparedDecision ;
      client = Client.builder()
                                .apiKey(api_key)
                                .build();
      while (true){

          String userInput = "";
          userInput = scanner.nextLine();
          if(userInput.equalsIgnoreCase("exit")){
              System.out.println("Exiting...");
              scanner.close();
              return;
          }
          try{
            String fullPrompt = promptContent  + 
            "\nCurrent user question: " + userInput;
           
            GenerateContentResponse response =
              client.models.generateContent(
                "gemini-2.5-flash",
                fullPrompt,
                null );
            preparedDecision = response.text().split("#");
            if (preparedDecision[0] == "9001"){
              System.out.println("This question is not technical nor related to billing");
            }else{
              router.agentRouter(preparedDecision, userInput, api_key,scanner);
            }
            
            System.out.println("AI agent response:");      
            System.out.println(response.text()); 
          }catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
          } 
      }

    }
}