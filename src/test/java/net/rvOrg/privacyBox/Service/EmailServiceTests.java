package net.rvOrg.privacyBox.Service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EmailServiceTests {

    @Autowired
    private EmailService emailService;

    @Test
    public void testEmail(){
        emailService.sendEmail("raghvendra.singh174852@gmail.com", "Testing Java Email Sender", "Hi, this is my first mail from Java Email Sender");
    }
}
