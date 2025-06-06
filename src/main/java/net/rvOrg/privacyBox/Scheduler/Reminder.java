package net.rvOrg.privacyBox.Scheduler;

import lombok.extern.slf4j.Slf4j;
import net.rvOrg.privacyBox.Entity.JournalEntry;
import net.rvOrg.privacyBox.Entity.UserEntity;
import net.rvOrg.privacyBox.Repository.JournalEntryRepository;
import net.rvOrg.privacyBox.Service.EmailService;
import net.rvOrg.privacyBox.Service.JournalEntryService;
import net.rvOrg.privacyBox.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Slf4j
@Component
public class Reminder {

    @Autowired
    private JournalEntryRepository journalEntryRepository;

    @Autowired
    private EmailService emailService;


//    @Scheduled(fixedRate = 60000)
    public void searchReminder(){
        LocalDateTime nowIST = LocalDateTime.now(ZoneId.of("Asia/Kolkata"));
        List<JournalEntry> journalEntryList = journalEntryRepository.findByReminderStatusTrueAndScheduledTimeLessThanEqual(nowIST);
        for(JournalEntry journalEntry: journalEntryList){
            try {
                String fullEmail = journalEntry.getUserEmail();
                String username = fullEmail.substring(0, fullEmail.indexOf('@'));

                String subject = "Revisorr: Gentle Reminder to Revise Your Material";
                String message = "Hello " + username + ",\n\n"
                        + "This is a friendly reminder from Revisor to review your material titled: \"" + journalEntry.getTitle() + "\".\n\n"
                        + "To continue your learning journey, please log in to your Revisorr account:\n"
                        + "https://revisorr.netlify.app\n\n"
                        + "Stay consistent, and happy revising!\n\n"
                        + "— The Revisorr Team";

                emailService.sendEmail(fullEmail, subject, message);
                journalEntry.setReminderStatus(false);
                journalEntryRepository.save(journalEntry);
                log.info("Mail send successfully");
            } catch (Exception e) {
                log.info("Error during email sending "+e);
            }
        }
    }
}
