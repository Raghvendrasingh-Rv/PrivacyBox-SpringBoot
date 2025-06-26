package net.rvOrg.privacyBox.Controller;

import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ollama/chatbot")
public class ChatbotController {

    @Autowired
    private VectorStore vectorStore;

    private final OllamaChatModel chatModel;

    public ChatbotController(OllamaChatModel chatModel) {
        this.chatModel = chatModel;
    }

    @GetMapping("/{message}")
    public ResponseEntity<?> askChatbot(@PathVariable String message) {
        try {
            // Step 1: Retrieve relevant documents from vector store
            var relevantDocs = vectorStore.similaritySearch(message);

            // Step 2: Build context from retrieved documents
            String context = buildContext(relevantDocs);

            // Step 3: Create enhanced prompt with context
            String enhancedPrompt = String.format("""
                You are a helpful assistant that answers questions based on the user's journal entries.
                
                Context from user's journal entries:
                %s
                
                User question: "%s"
                
                Instructions:
                - Answer based on the provided context from the user's journal entries
                - If no relevant context is found, say "I don't have enough information from your journal entries to answer this question"
                - Be personal and reference specific entries when relevant
                - Keep responses concise and helpful
                
                Answer:
                """, context.isEmpty() ? "I don't have enough information!" : context, message);

            // Step 4: Generate response
            ChatResponse response = chatModel.call(new Prompt(enhancedPrompt));
            String answer = response.getResult().getOutput().getText();

//            return new ResponseEntity<>(new SimpleResponse(answer, relevantDocs.size()), HttpStatus.OK);
            return new ResponseEntity<>(answer, HttpStatus.OK);

        } catch (Exception e) {
            System.err.println("Error in chatbot: " + e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>("Sorry, I encountered an error processing your request.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private String buildContext(List<?> documents) {
        if (documents.isEmpty()) {
            return "";
        }

        StringBuilder context = new StringBuilder();
        for (Object doc : documents) {
            try {
                // Use reflection to get content and metadata
                var docClass = doc.getClass();
                var textMethod = docClass.getMethod("getText");
                var metadataMethod = docClass.getMethod("getMetadata");

                String content = (String) textMethod.invoke(doc);
                var metadata = (java.util.Map<String, Object>) metadataMethod.invoke(doc);

                String title = (String) metadata.get("title");
                String category = (String) metadata.get("category");

                context.append("Entry: ").append(title != null ? title : "Untitled").append("\n");
                context.append("Category: ").append(category != null ? category : "PERSONAL").append("\n");
                context.append("Content: ").append(content.length() > 300 ? content.substring(0, 300) + "..." : content);
                context.append("\n---\n");

            } catch (Exception e) {
                // Fallback: just use toString()
                context.append(doc.toString()).append("\n---\n");
            }
        }

        return context.toString();
    }
}