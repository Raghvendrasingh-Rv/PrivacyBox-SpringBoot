package net.rvOrg.privacyBox.Repository;

import net.rvOrg.privacyBox.Entity.SharedEntity;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SharedRepository extends MongoRepository<SharedEntity, ObjectId> {

    List<SharedEntity> findByReceiver_Id(ObjectId myId);
    List<SharedEntity> findBySender_Id(ObjectId myId);
    List<SharedEntity> findByJournal_Id(ObjectId myId);
}
