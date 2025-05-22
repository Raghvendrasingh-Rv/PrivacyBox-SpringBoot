package net.rvOrg.privacyBox.Repository;

import net.rvOrg.privacyBox.Entity.SharedEntity;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SharedRepository extends MongoRepository<SharedEntity, ObjectId> {

    List<SharedEntity> findByReceiverId(ObjectId myId);
    List<SharedEntity> findBySenderId(ObjectId myId);
}
