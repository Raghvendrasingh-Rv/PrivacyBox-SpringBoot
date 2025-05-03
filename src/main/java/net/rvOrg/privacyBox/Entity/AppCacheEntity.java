package net.rvOrg.privacyBox.Entity;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("appcache")
@Getter
@Setter
public class AppCacheEntity {

    @Id
    private ObjectId id;
    private String key;
    private String value;
}
