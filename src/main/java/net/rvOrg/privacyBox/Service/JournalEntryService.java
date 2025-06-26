package net.rvOrg.privacyBox.Service;

import net.rvOrg.privacyBox.Entity.JournalEntry;
import net.rvOrg.privacyBox.Entity.SharedEntity;
import net.rvOrg.privacyBox.Entity.UserEntity;
import net.rvOrg.privacyBox.Repository.JournalEntryRepository;
import net.rvOrg.privacyBox.Repository.SharedRepository;
import org.bson.types.ObjectId;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Component
public class JournalEntryService {

    @Autowired
    private EmbeddingModel embeddingModel;

    @Autowired
    private VectorStore vectorStore;

    @Autowired
    private JournalEntryRepository journalEntryRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private SharedRepository sharedRepository;

    public List<JournalEntry> gelAll(){
        return journalEntryRepository.findAll();
    }

    @Transactional
    public void createEntry(JournalEntry entry, String username){
        try{
            UserEntity user = userService.findUser(username);
            entry.setUserEmail(user.getEmail());
            JournalEntry saved = journalEntryRepository.save(entry);
            user.getJournalEntries().add(saved);
            userService.updateUser(user);

            // Now add to vector store with the actual saved ID
            String combinedText = saved.getTitle() + "\n\n" + saved.getContent();

            // Create metadata map with the actual saved ID
            Map<String, Object> metadata = new HashMap<>();
            metadata.put("userEmail", saved.getUserEmail());
            metadata.put("category", saved.getCategory().toString());
            metadata.put("journalId", saved.getId().toString());
            metadata.put("title", saved.getTitle());
            metadata.put("createdAt", saved.getDate());

            // Create a Spring AI Document with content and metadata
            Document doc = new Document(combinedText, metadata);

            // Add to vector store (MongoDB Atlas)
            System.out.println("Adding document to vector store: " + combinedText.substring(0, Math.min(50, combinedText.length())));
            vectorStore.add(List.of(doc));
            System.out.println("Successfully added document to vector store");
        }
        catch(Exception e){
            System.out.println(e);
            throw new RuntimeException("An error occured while saving Journal", e);
        }
    }

    public void updateEntry(JournalEntry entry){
        journalEntryRepository.save(entry);
    }

    public Optional<JournalEntry> getJournalById(ObjectId myId){
        return journalEntryRepository.findById(myId);
    }

    @Transactional
    public boolean deleteJournalById(String username, ObjectId myId){
        boolean removed = false;
        try{
            UserEntity user = userService.findUser(username);
            removed = user.getJournalEntries().removeIf(entity -> entity.getId().equals(myId));
            List<SharedEntity> sharedEntities= sharedRepository.findByJournal_Id(myId);

            for(SharedEntity sharedEntry: sharedEntities){
                sharedRepository.delete(sharedEntry);
            }
            if(removed){
                userService.updateUser(user);
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
