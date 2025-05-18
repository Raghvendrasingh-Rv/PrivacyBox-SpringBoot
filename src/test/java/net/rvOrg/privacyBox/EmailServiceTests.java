package net.rvOrg.privacyBox;

import net.rvOrg.privacyBox.Scheduler.Reminder;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EmailServiceTests {

    @Autowired
    private Reminder reminder;

    @Test
    @Disabled
    public void test(){
        if (reminder != null) {
            reminder.searchReminder();
            System.out.println("Email sent");
        } else {
            System.out.println("Reminder bean not found.");
        }
    }
}
