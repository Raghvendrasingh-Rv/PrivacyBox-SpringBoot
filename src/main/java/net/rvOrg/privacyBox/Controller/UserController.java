package net.rvOrg.privacyBox.Controller;

import lombok.extern.slf4j.Slf4j;
import net.rvOrg.privacyBox.Entity.UserEntity;
import net.rvOrg.privacyBox.Repository.UserRepository;
import net.rvOrg.privacyBox.Service.UserService;
import net.rvOrg.privacyBox.Service.WeatherService;
import net.rvOrg.privacyBox.Utils.JwtUtils;
import net.rvOrg.privacyBox.api.response.WeatherResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.authentication.AuthenticationManager;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WeatherService weatherService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtils jwtUtils;

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @GetMapping("/userInfo")
    public ResponseEntity<?> getUserInfo(){
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            UserEntity user = userService.findUser(username);
            if(user!=null){
                return new ResponseEntity<>(user, HttpStatus.FOUND);
            }else{
                return new ResponseEntity<>("User not found!", HttpStatus.NOT_FOUND);
            }
        }catch(Exception err){
            log.error("User might not authorized", err);
            return new ResponseEntity<>("Incorrect Credentials", HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/updateUser")
    public ResponseEntity<?> updateUser(@RequestBody UserEntity user){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UserEntity userIndb = userService.findUser(username);
        UserEntity existingUser = userService.findUser(user.getUsername());
        if(existingUser!=null){
            return new ResponseEntity<>("This username already exist!!",HttpStatus.CONFLICT);
        }
        System.out.println("Credential before update:" + userIndb.getUsername() +" "+ userIndb.getPassword());
        if (user.getUsername() != null && !user.getUsername().isEmpty()) {
            userIndb.setUsername(user.getUsername());
        }
        if (user.getEmail() != null && !user.getEmail().isEmpty()) {
            userIndb.setEmail(user.getEmail());
        }
        userService.updateUser(userIndb);
        System.out.println("Credentials after update:" + userIndb.getUsername() +" "+ userIndb.getPassword());
        log.info("User updated");

        //refresh token
        try{
            UserDetails userDetails = userDetailsService.loadUserByUsername(userIndb.getUsername());
            String token = jwtUtils.generateToken(userDetails.getUsername());

            Map<String, Object> response = new HashMap<>();
            response.put("user", userIndb);
            response.put("token", token);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Token generation failed", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Token refresh failed");
        }

    }

    private ResponseEntity<?> deleteUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        userRepository.deleteByUsername(authentication.getName());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/weather")
    private ResponseEntity<?> getWeather(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        WeatherResponse weatherResponse = weatherService.getWeather("Mumbai");
        int weather  = 0;
        if(weatherResponse!=null){
            weather = weatherResponse.getCurrent().getTemperature();
        }
        return new ResponseEntity<>("Hi " + authentication.getName()+", Weather feels like " + weather ,HttpStatus.OK);
    }

}
