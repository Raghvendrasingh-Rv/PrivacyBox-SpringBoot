package net.rvOrg.privacyBox.Repository;

import net.rvOrg.privacyBox.Entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

public class UserRepositoryImpl {

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<UserEntity> getUserForSA(){
        Query query = new Query();
//        query.addCriteria(Criteria.where("username").is("1"));
//        query.addCriteria(Criteria.where("age").gte("10"));

        query.addCriteria(Criteria.where("email").regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-z|A-Z]{2,6}$"));
        query.addCriteria(Criteria.where("sentimentAnalysis").is(true));
        List<UserEntity> users = mongoTemplate.find(query, UserEntity.class);
        return users;
    }
}
