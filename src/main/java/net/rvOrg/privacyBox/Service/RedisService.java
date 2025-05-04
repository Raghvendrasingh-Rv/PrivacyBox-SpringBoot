package net.rvOrg.privacyBox.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class RedisService {

    @Autowired
    private RedisTemplate redisTemplate;


    public <T> T getDataIntoRedis(String key, Class<T> entityClass){
        try{
            Object res = redisTemplate.opsForValue().get(key);
            if (res == null) {
                log.warn("No value found for key "+ key);
                return null;
            }
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(res.toString(), entityClass);
        }catch(Exception e){
            log.error("Exception while get ",e);
            return null;
        }
    }

    public void setDataIntoRedis(String key, Object o, Long ttl){
        try{
            ObjectMapper mapper = new ObjectMapper();
            String jsonValue = mapper.writeValueAsString(o);
            redisTemplate.opsForValue().set(key, jsonValue, ttl, TimeUnit.SECONDS);
        }catch(Exception e){
            log.error("Exception while set ",e);
        }
    }
}
