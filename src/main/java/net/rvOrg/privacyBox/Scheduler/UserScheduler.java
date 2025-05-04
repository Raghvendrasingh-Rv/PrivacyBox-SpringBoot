package net.rvOrg.privacyBox.Scheduler;

import net.rvOrg.privacyBox.Config.AppCache;
import net.rvOrg.privacyBox.Entity.JournalEntry;
import net.rvOrg.privacyBox.Entity.UserEntity;
import net.rvOrg.privacyBox.Enums.Sentiments;
import net.rvOrg.privacyBox.Repository.UserRepositoryImpl;
import net.rvOrg.privacyBox.Service.EmailService;
import net.rvOrg.privacyBox.Service.UserSentimentAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
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


    @Scheduled(cron = "0 0 9 * * SUN")
    public void fetchUserAndSendEmail(){
        List<UserEntity> users = userRepositoryImpl.getUserForSA();
        for(UserEntity user: users){
           List<JournalEntry> journalEntries =  user.getJournalEntries();
           List<Sentiments> sentiments = journalEntries.stream().filter(x -> x.getDate().isAfter(LocalDateTime.now().minus(7, ChronoUnit.DAYS))).map(x -> x.getSentiments()).collect(Collectors.toList());

           Map<Sentiments, Integer> sentimentCount =  new HashMap<>();

           for(Sentiments one: sentiments){
               if(one!=null){
                   sentimentCount.put(one, sentimentCount.getOrDefault(one,0)+1);
               }
           }

           Sentiments maxCountSentiment = null;
           int maxCount =0;

           for(Map.Entry<Sentiments, Integer> i : sentimentCount.entrySet()){
               if(i.getValue()>maxCount){
                   maxCountSentiment = i.getKey();
                   maxCount = i.getValue();
               }
           }
           if(maxCountSentiment!=null){
           emailService.sendEmail(user.getEmail(), "Sentiment for last 7 days - Yash pr bharosha mt krna", maxCountSentiment.toString());
           }
        }
    }

    @Scheduled(cron = "0 0/10 * ? * *")
    public void refreshCache(){
        appCache.init();
    }
}
