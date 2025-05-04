package net.rvOrg.privacyBox.Entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import net.rvOrg.privacyBox.Enums.Sentiments;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Date;

@Document(collection = "journalEntry")
@Data
@NoArgsConstructor
public class JournalEntry {

    @Id
    private ObjectId id;
    private String title;
    private String content;
    private LocalDateTime date;
    private Sentiments sentiments;

}
