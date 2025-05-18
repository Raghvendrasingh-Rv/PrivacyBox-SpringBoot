package net.rvOrg.privacyBox.Service;

import net.rvOrg.privacyBox.Entity.UserEntity;
import net.rvOrg.privacyBox.Repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public List<UserEntity> gelAllUser(){
        return userRepository.findAll();
    }

    public void createUser(UserEntity entry){
        userRepository.save(entry);
    }

    public void createNewUser(UserEntity entry){
        entry.setPassword(passwordEncoder.encode(entry.getPassword()));
        entry.setRoles(Arrays.asList("USER"));
        userRepository.save(entry);
    }

    public void updateUser(UserEntity user){
        userRepository.save(user);
    }
    
    

    public boolean deleteUser(ObjectId myId){
        userRepository.deleteById(myId);
        return true;
    }

    public UserEntity findUser(String username){
        return userRepository.findByUsername(username);
    }
}
