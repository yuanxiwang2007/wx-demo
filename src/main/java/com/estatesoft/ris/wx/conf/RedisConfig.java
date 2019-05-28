package com.estatesoft.ris.wx.conf;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.session.data.redis.config.annotation.web.http.RedisHttpSessionConfiguration;
import org.springframework.session.web.http.DefaultCookieSerializer;

@Configuration
public class RedisConfig {
    @Value("${redis.applicationName}")
    private String redisApplicationName;
    @Bean
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory redisFactory){
        StringRedisTemplate template = new StringRedisTemplate(redisFactory);
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new
                Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        template.setValueSerializer(jackson2JsonRedisSerializer);
        template.setHashValueSerializer(jackson2JsonRedisSerializer);
        template.afterPropertiesSet();
//        template.setEnableTransactionSupport(true);
        return template;
    }
    @Bean
    public DefaultCookieSerializer defaultCookieSerializer(){
        DefaultCookieSerializer defaultCookieSerializer=new DefaultCookieSerializer();
//        defaultCookieSerializer.setDomainName("urine-miniapp.doctorwork.com");
        defaultCookieSerializer.setCookiePath("/");
        defaultCookieSerializer.setCookieName(redisApplicationName + "_session_id");
        return defaultCookieSerializer;
    }
    @Bean
    public RedisHttpSessionConfiguration redisHttpSessionConfiguration(DefaultCookieSerializer defaultCookieSerializer){
        RedisHttpSessionConfiguration redisHttpSessionConfiguration=new RedisHttpSessionConfiguration();
        redisHttpSessionConfiguration.setCookieSerializer(defaultCookieSerializer);
        redisHttpSessionConfiguration.setMaxInactiveIntervalInSeconds(604800);
        return redisHttpSessionConfiguration;
    }
    @Bean
    public TaskScheduler scheduledExecutorService() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(8);
        scheduler.setThreadNamePrefix("scheduled-thread-");
        return scheduler;
    }
}