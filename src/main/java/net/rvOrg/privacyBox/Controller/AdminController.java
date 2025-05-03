package net.rvOrg.privacyBox.Controller;

import net.rvOrg.privacyBox.Config.AppCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AppCache appCache;

    @GetMapping("/clearCache")
    public void clearCache(){
        appCache.init();
    }
}
