package net.rvOrg.privacyBox.Service;

import net.rvOrg.privacyBox.Entity.UserEntity;
import net.rvOrg.privacyBox.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchService {

    @Autowired
    private UserRepository userRepository;

    public List<UserEntity> searchUser(String username){
        return userRepository.findByUsernameRegex(username);
    }
}
