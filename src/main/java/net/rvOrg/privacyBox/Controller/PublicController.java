package net.rvOrg.privacyBox.Controller;

import net.rvOrg.privacyBox.Entity.UserEntity;
import net.rvOrg.privacyBox.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")
public class PublicController {

    @Autowired
    private UserService userService;

    @PostMapping("/createUser")
    public ResponseEntity<?> createUser(@RequestBody UserEntity user){
        userService.createNewUser(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
