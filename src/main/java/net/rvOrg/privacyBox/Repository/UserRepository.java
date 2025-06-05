package net.rvOrg.privacyBox.Repository;

import net.rvOrg.privacyBox.Entity.UserEntity;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface UserRepository extends MongoRepository<UserEntity, ObjectId> {

    UserEntity findByUsername(String username);
    void deleteByUsername(String username);

    @Query("{'username': {$regex: ?0}}")
    List<UserEntity> findByUsernameRegex(String username);
}
