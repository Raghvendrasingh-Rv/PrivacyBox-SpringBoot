package net.rvOrg.privacyBox.Service;

import net.rvOrg.privacyBox.Scheduler.Reminder;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ReminderTests {

    @Autowired
    private Reminder reminder;

    @Disabled
    @Test
    public void TestReminder(){
        reminder.searchReminder();
        System.out.println(" ");
    }
}
