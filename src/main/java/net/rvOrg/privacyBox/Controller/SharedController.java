package net.rvOrg.privacyBox.Controller;

import lombok.extern.slf4j.Slf4j;
import net.rvOrg.privacyBox.Entity.SharedEntity;
import net.rvOrg.privacyBox.Entity.UserEntity;
import net.rvOrg.privacyBox.Service.SharedService;
import net.rvOrg.privacyBox.Service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/shared")
@Slf4j
public class SharedController {

    @Autowired
    private SharedService sharedService;

    @Autowired
    private UserService userService;

    @PostMapping("/createSharedEntry")
    public ResponseEntity<?> createSharedEntry(@RequestBody SharedEntity entry){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try{
            entry.setSentTime(Instant.now());
            sharedService.createSharedEntry(entry);
            return new ResponseEntity<>(entry, HttpStatus.CREATED);
        }catch(Exception err){
            System.out.println(err);
            return new ResponseEntity<>("Error while creating shared entry", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getReceiverEntry")
    public ResponseEntity<?> getReceiverEntry(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try{
            String username = authentication.getName();
            UserEntity user = userService.findUser(username);
            ObjectId myId = user.getId();
            List<SharedEntity> sharesList = sharedService.findByReceiverId(myId);
            return new ResponseEntity<>(sharesList, HttpStatus.FOUND);
        }catch(Exception err){
            log.error("Error while fetching receiver shared list",err);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getSenderEntry")
    public ResponseEntity<?> getSenderEntry(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try{
            String username = authentication.getName();
            UserEntity user = userService.findUser(username);
            ObjectId myId = user.getId();
            List<SharedEntity> sharesList = sharedService.findBySenderId(myId);
            return new ResponseEntity<>(sharesList, HttpStatus.FOUND);
        }catch(Exception err){
            log.error("Error while fetching sender shared list",err);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
