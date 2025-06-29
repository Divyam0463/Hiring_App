package com.example.Service;

import com.example.DTO.CandidateDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class RedisService {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public <T> List<T> get(String key, Class<T> clazz) {
        try {
            String json = redisTemplate.opsForValue().get(key);
            if (json == null) return null;

            CollectionType listType = objectMapper.getTypeFactory()
                    .constructCollectionType(List.class, clazz);

            return objectMapper.readValue(json, listType);
        } catch (Exception e) {
            log.error("Redis get() exception", e);
            return null;
        }
    }

    public void set(String key, Object value, Long ttl){
        try{
            String json = objectMapper.writeValueAsString(value) ;
            redisTemplate.opsForValue().set(key,
                    json,
                    ttl,
                    TimeUnit.SECONDS);
        }catch (Exception e){
            log.error(e.getMessage(),"Exception caught");
        }
    }
}
