package net.rvOrg.privacyBox.Entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import net.rvOrg.privacyBox.Enums.Category;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.time.Instant;
import java.time.LocalDateTime;

@Document(collection = "journalEntry")
@Data
@NoArgsConstructor
public class JournalEntry {

    @Id
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;
    private String title;
    private String content;
    private String fileType = null;
    private String fileUrl = null;
    private Instant date;
    private LocalDateTime scheduledTime;
    private String userEmail;
    private Category category;
    private boolean reminderStatus;

}
