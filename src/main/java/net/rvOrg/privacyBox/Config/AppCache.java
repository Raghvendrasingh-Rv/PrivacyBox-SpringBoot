package net.rvOrg.privacyBox.Config;

import jakarta.annotation.PostConstruct;
import net.rvOrg.privacyBox.Entity.AppCacheEntity;
import net.rvOrg.privacyBox.Repository.AppCacheRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AppCache {

    public enum keys{
        weatherApi;
    }


    @Autowired
    private AppCacheRepository appCacheRepository;

    public static Map<String,String> appCacheList;

    @PostConstruct
    public void init(){
        appCacheList = new HashMap<>();
        List<AppCacheEntity> all = appCacheRepository.findAll();
        for(AppCacheEntity one : all){
            appCacheList.put(one.getKey(), one.getValue());
        }
    }
}
