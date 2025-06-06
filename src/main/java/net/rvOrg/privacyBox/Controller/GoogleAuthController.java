package net.rvOrg.privacyBox.Controller;


import lombok.extern.slf4j.Slf4j;
import net.rvOrg.privacyBox.Entity.UserEntity;
import net.rvOrg.privacyBox.Repository.UserRepository;
import net.rvOrg.privacyBox.Service.CustomUserDetailsServiceImpl;
import net.rvOrg.privacyBox.Utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.time.Duration;
import java.util.*;

@RestController
@RequestMapping("/auth/google")
@Slf4j
public class GoogleAuthController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private CustomUserDetailsServiceImpl userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Value("${GOOGLE_CLIENT_ID}")
    private String clientId;
    @Value("${GOOGLE_CLIENT_SECRET}")
    private String clientSecret;

    @Value("${FRONTEND_PROD_ENDPOINT}")
    private String frontendProdEndpoint;

    @Value("${BACKEND_PROD_ENDPOINT}")
    private String backendProdEndpoint;


    @GetMapping("callback")
    public ResponseEntity<?> handleGoogleCallback(@RequestParam String code){
        try{
            String tokenEndpoint = "https://oauth2.googleapis.com/token";

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("code", code);
            params.add("client_id", clientId);
            params.add("client_secret", clientSecret);
            params.add("redirect_uri", backendProdEndpoint+"/auth/google/callback");
            params.add("grant_type", "authorization_code");

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
            ResponseEntity<Map> tokenResponse = restTemplate.postForEntity(tokenEndpoint, request, Map.class);
            String idToken = (String) tokenResponse.getBody().get("id_token");
            System.out.println(idToken);
            String userInfoUrl = "https://oauth2.googleapis.com/tokeninfo?id_token=" + idToken;

            ResponseEntity<Map> userInfoResponse = restTemplate.getForEntity(userInfoUrl, Map.class);
            if(userInfoResponse.getStatusCode()== HttpStatus.OK){
                Map<String, Object> userInfo = userInfoResponse.getBody();
                String email = (String) userInfo.get("email");
                UserDetails userDetails = null;
                try{
                    userDetails = userDetailsService.loadUserByUsername(email);
                }catch(Exception e){
                    UserEntity user = new UserEntity();
                    user.setEmail(email);
                    user.setUsername(email);
                    user.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
                    user.setRoles(Arrays.asList("USER"));
                    userRepository.save(user);
                }
                String jwtToken = jwtUtils.generateToken(email);

                ResponseCookie cookie = ResponseCookie.from("token", jwtToken)
                        .httpOnly(false)
                        .secure(true) // ⚠️
                        .sameSite("None")
                        .path("/")
                        .maxAge(Duration.ofDays(1))
                        .build();

                // Redirect to frontend success page (no token in URL)
                return ResponseEntity.status(HttpStatus.FOUND)
                        .header(HttpHeaders.SET_COOKIE, cookie.toString())
                        .location(URI.create(frontendProdEndpoint+"/googleLoginSuccessPage"))
                        .build();
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }catch(Exception e){
            log.error("Exception occured while handle google callback", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
