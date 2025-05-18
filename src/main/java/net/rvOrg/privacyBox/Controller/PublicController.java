package net.rvOrg.privacyBox.Controller;

import lombok.extern.slf4j.Slf4j;
import net.rvOrg.privacyBox.Entity.UserEntity;
import net.rvOrg.privacyBox.Service.UserService;
import net.rvOrg.privacyBox.Utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/public")
public class PublicController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/signupUser")
    public ResponseEntity<?> signup(@RequestBody UserEntity user){
        userService.createNewUser(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    @PostMapping("/loginUser")
    public ResponseEntity<?> login(@RequestBody UserEntity user){
        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
            UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
            String token = jwtUtils.generateToken(userDetails.getUsername());
            return new ResponseEntity<>(token, HttpStatus.CREATED);
        }catch(Exception e){
            log.error("Exception occurred while creating Authentication Token", e);
            return new ResponseEntity<>("Incorrect username or password",HttpStatus.BAD_REQUEST);
        }
    }

}
