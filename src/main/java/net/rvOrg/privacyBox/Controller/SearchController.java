package net.rvOrg.privacyBox.Controller;

import net.rvOrg.privacyBox.Entity.UserEntity;
import net.rvOrg.privacyBox.Service.SearchService;
import net.rvOrg.privacyBox.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/search")
public class SearchController {


    @Autowired
    private SearchService searchService;


    @GetMapping("/users")
    public ResponseEntity<?> searchUser(@RequestParam String username){
        List<UserEntity> list = searchService.searchUser(username);
        if(list!=null) {
            return new ResponseEntity<>(list, HttpStatus.FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
