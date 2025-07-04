package net.rvOrg.privacyBox.Scheduler;

import net.rvOrg.privacyBox.Config.AppCache;
import net.rvOrg.privacyBox.Entity.JournalEntry;
import net.rvOrg.privacyBox.Entity.UserEntity;
import net.rvOrg.privacyBox.Enums.Category;
import net.rvOrg.privacyBox.Model.SentimentData;
import net.rvOrg.privacyBox.Repository.UserRepositoryImpl;
import net.rvOrg.privacyBox.Service.EmailService;
import net.rvOrg.privacyBox.Service.UserSentimentAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class UserScheduler {

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserSentimentAnalysisService userSentimentAnalysisService;

    @Autowired
    private UserRepositoryImpl userRepositoryImpl;

    @Autowired
    private AppCache appCache;

    @Autowired
    private KafkaTemplate<String, SentimentData> kafkaTemplate;


    @Scheduled(cron = "0 0 9 * * SUN")
    public void fetchUserAndSendEmail(){
        List<UserEntity> users = userRepositoryImpl.getUserForSA();
        for(UserEntity user: users){
           List<JournalEntry> journalEntries =  user.getJournalEntries();
           List<Category> sentiments = journalEntries.stream().filter(x -> x.getDate().isAfter(Instant.now().minus(7, ChronoUnit.DAYS))).map(x -> x.getCategory()).collect(Collectors.toList());

           Map<Category, Integer> sentimentCount =  new HashMap<>();

           for(Category one: sentiments){
               if(one!=null){
                   sentimentCount.put(one, sentimentCount.getOrDefault(one,0)+1);
               }
           }

           Category maxCountSentiment = null;
           int maxCount =0;

           for(Map.Entry<Category, Integer> i : sentimentCount.entrySet()){
               if(i.getValue()>maxCount){
                   maxCountSentiment = i.getKey();
                   maxCount = i.getValue();
               }
           }
           if(maxCountSentiment!=null){
//           emailService.sendEmail(user.getEmail(), "Sentiment for last 7 days", maxCountSentiment.toString());
               SentimentData sentimentData = SentimentData.builder().email(user.getEmail()).sentiment("Sentiment for last 7 days " + maxCountSentiment).build();
               try{
                   kafkaTemplate.send("weekly-sentiments", sentimentData.getEmail(), sentimentData);
                   //Kafka Producer
               }catch(Exception e){
                   emailService.sendEmail(sentimentData.getEmail(), "Sentiment for last 7 days", sentimentData.getSentiment());
               }
           }
        }
    }

    @Scheduled(cron = "0 0/10 * ? * *")
    public void refreshCache(){
        appCache.init();
    }
}
