package net.rvOrg.privacyBox.Controller;
import net.rvOrg.privacyBox.Scheduler.Reminder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cron")
public class CronController {

    @Autowired
    private Reminder reminder;

    private static final String SECRET_KEY = "my-secure-key-1234"; // replace with a strong key

    @GetMapping("/run")
    public ResponseEntity<String> runReminder(@RequestParam("key") String key) {
        if (!SECRET_KEY.equals(key)) {
            return ResponseEntity.status(403).body("Forbidden");
        }
        reminder.searchReminder();
        return ResponseEntity.ok("Reminder executed.");
    }
}
