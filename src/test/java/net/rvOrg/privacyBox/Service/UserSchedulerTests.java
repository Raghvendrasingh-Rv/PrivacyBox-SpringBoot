package net.rvOrg.privacyBox.Service;

import net.rvOrg.privacyBox.Scheduler.UserScheduler;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserSchedulerTests {

    @Autowired
    private UserScheduler userScheduler;

    @Disabled("Reason for disabling this specific test")
    @Test
    public void testEmail(){
        userScheduler.fetchUserAndSendEmail();
    }
}
