package net.rvOrg.privacyBox.Service;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
public class RedisTests {

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    @Disabled
    void testRedis(){
        redisTemplate.opsForValue().set("email", "1@gmail.com");
        Object gmail = redisTemplate.opsForValue().get("name");
        int a=1;
    }
}
