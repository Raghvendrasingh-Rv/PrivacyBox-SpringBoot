package net.rvOrg.privacyBox.Controller;

import net.rvOrg.privacyBox.Scheduler.Reminder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    private Reminder reminder;

    @GetMapping("/test-email")
    public String testEmail() {
        reminder.searchReminder();
        return "Reminder executed";
    }
}
