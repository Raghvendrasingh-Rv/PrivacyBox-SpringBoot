package net.rvOrg.privacyBox.Entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Document(collection = "sharedTable")
public class SharedEntity {

    @Id
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId id;
    private JournalEntry journal;
    private UserEntity sender;
    private UserEntity receiver;
    private boolean seen;
    private Instant sentTime;
}
