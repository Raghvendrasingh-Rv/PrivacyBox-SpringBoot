package net.rvOrg.privacyBox.Entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "sharedTable")
public class SharedEntity {

    @Id
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;
    private JournalEntry journalId;
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId senderId;
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId receiverId;
    private boolean seen;
}
