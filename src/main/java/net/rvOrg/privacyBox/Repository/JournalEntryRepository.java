package net.rvOrg.privacyBox.Repository;

import net.rvOrg.privacyBox.Entity.JournalEntry;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface JournalEntryRepository extends MongoRepository<JournalEntry, ObjectId> {

    List<JournalEntry> findByReminderStatusTrueAndScheduledTimeBefore(LocalDateTime time);
}
