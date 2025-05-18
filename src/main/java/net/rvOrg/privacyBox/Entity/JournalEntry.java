package net.rvOrg.privacyBox.Entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import net.rvOrg.privacyBox.Enums.Sentiments;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.time.LocalDateTime;
import java.util.Date;

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
    private LocalDateTime date;
    private LocalDateTime scheduledTime;
    private String userEmail;
    private Sentiments sentiments;
    private boolean reminderStatus;

}
