package net.rvOrg.privacyBox.Service;

import net.rvOrg.privacyBox.Entity.SharedEntity;
import net.rvOrg.privacyBox.Repository.SharedRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SharedService {

    @Autowired
    private SharedRepository sharedRepository;

    public void createSharedEntry(SharedEntity entry){
           SharedEntity saved = sharedRepository.save(entry);
    }

    public List<SharedEntity> findByReceiverId(ObjectId myId){
        return sharedRepository.findByReceiverId(myId);
    }

    public List<SharedEntity> findBySenderId(ObjectId myId){
        return sharedRepository.findBySenderId(myId);
    }
}
