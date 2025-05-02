package net.rvOrg.privacyBox.Repository;

import net.rvOrg.privacyBox.Entity.UserEntity;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<UserEntity, ObjectId> {

    UserEntity findByUsername(String username);
    void deleteByUsername(String username);
}
