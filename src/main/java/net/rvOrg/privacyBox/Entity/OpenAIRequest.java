package net.rvOrg.privacyBox.Entity;

import java.util.List;

public class OpenAIRequest {
    public String model = "openai/gpt-3.5-turbo";
    public List<Message> messages;

    public static class Message {
        public String role;
        public String content;

        public Message(String role, String content) {
            this.role = role;
            this.content = content;
        }
    }

    public OpenAIRequest(List<Message> messages) {
        this.messages = messages;
    }
}
