package net.rvOrg.privacyBox.Service;

import net.rvOrg.privacyBox.Config.AppCache;
import net.rvOrg.privacyBox.Repository.AppCacheRepository;
import net.rvOrg.privacyBox.api.response.WeatherResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Component
public class WeatherService {

//    private static final String apiKey;

    @Value("${WEATHER_API_KEY}")
    private String apiKey;


    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RedisService redisService;

    public WeatherResponse getWeather(String city){
        WeatherResponse weatherRedisResponse = redisService.getDataIntoRedis("weather_of_" + city, WeatherResponse.class);
        if(weatherRedisResponse!=null){
            return weatherRedisResponse;
        }else{
        String finalAPI = AppCache.appCacheList.get(AppCache.keys.weatherApi.toString()).replace("<city>", city).replace("<api_key>", apiKey);
        ResponseEntity<WeatherResponse> response = restTemplate.exchange(finalAPI, HttpMethod.GET, null, WeatherResponse.class);
        WeatherResponse body = response.getBody();
        if(body!=null){
            redisService.setDataIntoRedis("weather_of_"+ city, body, 300l);
        }
        return body;
        }
    }
}
