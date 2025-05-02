package net.rvOrg.privacyBox.Service;

import net.rvOrg.privacyBox.Entity.JournalEntry;
import net.rvOrg.privacyBox.Entity.UserEntity;
import net.rvOrg.privacyBox.Repository.JournalEntryRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Service
@Component
public class JournalEntryService {

    @Autowired
    private JournalEntryRepository journalEntryRepository;

    @Autowired
    private UserService userService;

    public List<JournalEntry> gelAll(){
        return journalEntryRepository.findAll();
    }

    @Transactional
    public void createEntry(JournalEntry entry, String username){
        try{
            JournalEntry saved = journalEntryRepository.save(entry);
            UserEntity user = userService.findUser(username);
            user.getJournalEntries().add(saved);
            userService.createUser(user);
        }
        catch(Exception e){
            System.out.println(e);
            throw new RuntimeException("An error occured while saving Journal", e);
        }
    }

    public void createEntry(JournalEntry entry){
        journalEntryRepository.save(entry);
    }

    public Optional<JournalEntry> getJournalById(ObjectId myId){
        return journalEntryRepository.findById(myId);
    }

    public boolean deleteJournalById(String username, ObjectId myId){
        boolean removed = false;
        try{
            UserEntity user = userService.findUser(username);
            removed = user.getJournalEntries().removeIf(entity -> entity.getId().equals(myId));
            if(removed){
                userService.createUser(user);
                journalEntryRepository.deleteById(myId);
            }
        }
        catch (Exception e){
            System.out.println(e);
            throw new RuntimeException("An error occurred while deleting the entry", e);
        }
        return removed;
    }

    public Optional<JournalEntry> findById(ObjectId myId){
        return journalEntryRepository.findById(myId);
    }
}
