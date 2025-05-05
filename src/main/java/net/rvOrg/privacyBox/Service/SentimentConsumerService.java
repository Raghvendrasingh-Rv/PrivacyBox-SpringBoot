package net.rvOrg.privacyBox.Service;

import net.rvOrg.privacyBox.Model.SentimentData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class SentimentConsumerService {

    @Autowired
    private EmailService emailService;

    @KafkaListener(topics= "weekly-sentiments", groupId = "springboot-group-1")
    public void consumer(SentimentData sentimentData){
        sendEmail(sentimentData);
    }

    private void sendEmail(SentimentData sentimentData){
        emailService.sendEmail(sentimentData.getEmail(), "Sentiment for last 7 days", sentimentData.getSentiment());
    }
}
