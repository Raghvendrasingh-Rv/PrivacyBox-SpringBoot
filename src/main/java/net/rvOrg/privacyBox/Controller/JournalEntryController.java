package net.rvOrg.privacyBox.Controller;

import net.rvOrg.privacyBox.Entity.JournalEntry;
import net.rvOrg.privacyBox.Entity.UserEntity;
import net.rvOrg.privacyBox.Service.JournalEntryService;
import net.rvOrg.privacyBox.Service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.core.sync.RequestBody;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/journal")
public class JournalEntryController {
    @Autowired
    private JournalEntryService journalEntryService;

    @Autowired
    private UserService userService;

    @Autowired
    private S3Client s3Client;

    public String fileType;
    public String fileUrl;

    @GetMapping("/getAll")
    public ResponseEntity<List<JournalEntry>> getAllJournalOfUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity user = userService.findUser(authentication.getName());
        List<JournalEntry> list = user.getJournalEntries();
        if(list!=null && !list.isEmpty()){
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @PostMapping("/createEntry")
    public ResponseEntity<JournalEntry> createEntry(@org.springframework.web.bind.annotation.RequestBody JournalEntry entry){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try{
            entry.setDate(Instant.now());
            journalEntryService.createEntry(entry, authentication.getName());
            return new ResponseEntity<>(entry, HttpStatus.CREATED);
        }
        catch(Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getById/{myId}")
    public ResponseEntity<JournalEntry> getJournalById(@PathVariable ObjectId myId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity user = userService.findUser(authentication.getName());
        List<JournalEntry> collect = user.getJournalEntries().stream().filter(x -> x.getId().equals(myId)).collect(Collectors.toList());
        if(!collect.isEmpty()){
            Optional<JournalEntry> journalEntry = journalEntryService.findById(myId);
            return new ResponseEntity<>(journalEntry.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/delete/{myId}")
    public ResponseEntity<?> deleteJournalById(@PathVariable ObjectId myId){
        System.out.println("object:" + myId);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean removed = journalEntryService.deleteJournalById(authentication.getName(), myId);
        if(removed){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/update/{myId}")
    public ResponseEntity<?> UpdateJournalById(@PathVariable ObjectId myId, @org.springframework.web.bind.annotation.RequestBody JournalEntry newE){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity user = userService.findUser(authentication.getName());
        List<JournalEntry> collect = user.getJournalEntries().stream().filter(x -> x.getId().equals(myId)).collect(Collectors.toList());
        if(!collect.isEmpty()){
            JournalEntry oldE = journalEntryService.getJournalById(myId).orElse(null);
            if(oldE!=null){
                oldE.setTitle(newE.getTitle()!=null && !newE.getTitle().equals("") ? newE.getTitle(): oldE.getTitle());
                oldE.setContent(newE.getContent()!=null && !newE.getContent().equals("") ? newE.getContent(): oldE.getContent());
                oldE.setReminderStatus(newE.isReminderStatus());
                oldE.setScheduledTime(newE.getScheduledTime()!=null && !newE.getScheduledTime().equals("")?newE.getScheduledTime():oldE.getScheduledTime());
                oldE.setCategory(newE.getCategory()!=null && !newE.getCategory().equals("")?newE.getCategory():oldE.getCategory());
                journalEntryService.updateEntry(oldE);
                return new ResponseEntity<>(oldE, HttpStatus.OK);
            }

        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        fileType = file.getContentType();
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

        try {
            s3Client.putObject(
                    PutObjectRequest.builder()
                            .bucket("privacybox-uploads")
                            .key(fileName)
                            .contentType(file.getContentType())
                            .build(),
                    RequestBody.fromInputStream(file.getInputStream(), file.getSize())
            );

            fileUrl = "https://privacybox-uploads.s3.ap-south-1.amazonaws.com/" + fileName;

            Map<String, String> response = new HashMap<>();
            response.put("fileUrl", fileUrl);
            response.put("fileType", fileType);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload");
        }
    }
}
