package net.rvOrg.privacyBox.Controller;

import net.rvOrg.privacyBox.Entity.UserEntity;
import net.rvOrg.privacyBox.Repository.UserRepository;
import net.rvOrg.privacyBox.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @PutMapping("/updateUser")
    public ResponseEntity<?> updateUser(@RequestBody UserEntity user){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UserEntity userIndb = userService.findUser(username);
        UserEntity existingUser = userService.findUser(user.getUsername());
        if(existingUser!=null){
            return new ResponseEntity<>("Username already exist!!",HttpStatus.CONFLICT);
        }
        userIndb.setUsername(user.getUsername());
        userIndb.setPassword(user.getPassword());
        userService.createNewUser(userIndb);
        return new ResponseEntity<>(userIndb, HttpStatus.FOUND);
    }

    private ResponseEntity<?> deleteUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        userRepository.deleteByUsername(authentication.getName());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
