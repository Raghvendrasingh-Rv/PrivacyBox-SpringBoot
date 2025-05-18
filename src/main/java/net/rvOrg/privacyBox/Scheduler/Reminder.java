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
import java.util.List;

@Slf4j
@Component
public class Reminder {

    @Autowired
    private JournalEntryRepository journalEntryRepository;

    @Autowired
    private EmailService emailService;


    @Scheduled(fixedRate = 60000)
    public void searchReminder(){

        List<JournalEntry> journalEntryList = journalEntryRepository.findByReminderStatusTrueAndScheduledTimeBefore(LocalDateTime.now());
        for(JournalEntry journalEntry: journalEntryList){
            try {
                emailService.sendEmail(journalEntry.getUserEmail(), "Revisor: This a gentle reminder to revise your material", "Hello - Please revise you material with title: "+ journalEntry.getTitle()+" by login to the Revisor account");
                journalEntry.setReminderStatus(false);
                journalEntryRepository.save(journalEntry);
                log.info("Mail send successfully");
            } catch (Exception e) {
                log.info("Error during email sending "+e);
            }
        }
    }
}
