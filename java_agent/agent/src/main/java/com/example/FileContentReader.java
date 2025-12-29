package com.example;

 
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;

     

public class FileContentReader {
     public static String loadPrompt(String resourcePath) throws IOException {
        ClassLoader classLoader = FileContentReader.class.getClassLoader();

        try (InputStream is = classLoader.getResourceAsStream(resourcePath)) {
            if (is == null) {
                throw new IOException("Resource not found on classpath: " + resourcePath);
            }
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        }
    }
    public static HashSet<String> prepareDocumentation(HashSet<String> Docs, String[] keyWords, String regex){
        HashSet<String> preparedTechnicalDocs = new HashSet<>();
        for (String i:Docs){
            String content = "";
            try {
                content = FileContentReader.loadPrompt(i);
            } catch (IOException e) {
                System.err.println("Error reading technical document: " + i);
                e.printStackTrace();
                continue;
            }
            String[] paraghraps = content.split(regex);
            for (String p:paraghraps){
                for (String kw:keyWords){
                    if (p.toLowerCase().contains(kw.toLowerCase()) && p.length() < 1000){
                        preparedTechnicalDocs.add(p);
                    }
                }
            }
        }

        return preparedTechnicalDocs;
    }
    
}
