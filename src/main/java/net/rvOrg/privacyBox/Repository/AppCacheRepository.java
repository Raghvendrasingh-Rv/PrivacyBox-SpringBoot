package net.rvOrg.privacyBox.Repository;

import net.rvOrg.privacyBox.Entity.AppCacheEntity;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AppCacheRepository extends MongoRepository<AppCacheEntity, ObjectId> {
}
