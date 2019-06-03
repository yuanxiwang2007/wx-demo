package com.estatesoft.ris.wx.conf;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {
    @Value("${redis.applicationName}")
    private String redisApplicationName;
    @Bean
    public <T> RedisTemplate<String, T> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, T> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());

        Jackson2JsonRedisSerializer jsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);

        template.setHashValueSerializer(jsonRedisSerializer);
        template.setValueSerializer(jsonRedisSerializer);
        template.afterPropertiesSet();
        return template;
    }
//    @Bean(name = "redisTemplate")
//    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory factory) {
//        RedisTemplate<String, String> template = new RedisTemplate<>();
//        RedisSerializer<String> redisSerializer = new StringRedisSerializer();
//        template.setConnectionFactory(factory);
//        template.setKeySerializer(redisSerializer);
//        template.setValueSerializer(redisSerializer);
//        template.setHashValueSerializer(redisSerializer);
//        template.setHashKeySerializer(redisSerializer);
//        return template;
//    }
//    @Bean
//    @SuppressWarnings({ "rawtypes", "unchecked" })
//    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory redisFactory){
//        StringRedisTemplate template = new StringRedisTemplate(redisFactory);
//        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new
//                Jackson2JsonRedisSerializer(Object.class);
//        ObjectMapper om = new ObjectMapper();
//        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
//        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
//        jackson2JsonRedisSerializer.setObjectMapper(om);
//        template.setValueSerializer(jackson2JsonRedisSerializer);
//        template.setHashValueSerializer(jackson2JsonRedisSerializer);
//        template.afterPropertiesSet();
////        template.setEnableTransactionSupport(true);
//        return template;
//    }
//    @Bean
//    public DefaultCookieSerializer defaultCookieSerializer(){
//        DefaultCookieSerializer defaultCookieSerializer=new DefaultCookieSerializer();
////        defaultCookieSerializer.setDomainName("urine-miniapp.doctorwork.com");
//        defaultCookieSerializer.setCookiePath("/");
//        defaultCookieSerializer.setCookieName(redisApplicationName + "_session_id");
//        return defaultCookieSerializer;
//    }
//    @Bean
//    public RedisHttpSessionConfiguration redisHttpSessionConfiguration(DefaultCookieSerializer defaultCookieSerializer){
//        RedisHttpSessionConfiguration redisHttpSessionConfiguration=new RedisHttpSessionConfiguration();
//        redisHttpSessionConfiguration.setCookieSerializer(defaultCookieSerializer);
//        redisHttpSessionConfiguration.setMaxInactiveIntervalInSeconds(604800);
//        return redisHttpSessionConfiguration;
//    }
//    @Bean
//    public TaskScheduler scheduledExecutorService() {
//        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
//        scheduler.setPoolSize(8);
//        scheduler.setThreadNamePrefix("scheduled-thread-");
//        return scheduler;
//    }
}