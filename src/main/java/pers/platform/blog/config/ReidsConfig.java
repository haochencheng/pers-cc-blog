package pers.platform.blog.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

import java.lang.reflect.Method;

@Configuration
@EnableCaching
public class ReidsConfig extends CachingConfigurerSupport {

    @Bean
    public CacheManager cacheManager(
            RedisTemplate<String, String> redisTemplate) {
        RedisCacheManager rcm = new RedisCacheManager(redisTemplate);
        // 默认过期时间 s
        // rcm.setDefaultExpiration(3000);
        return rcm;
    }

    /*
     * 缓存key生成器
     */
    @Override
    @Bean
    public KeyGenerator keyGenerator() {
        return new KeyGenerator() {
            @SuppressWarnings("unused")
            private Object target;
            @SuppressWarnings("unused")
            private Method method;
            @SuppressWarnings("unused")
            private Object[] params;

            @Override
            public Object generate(Object target, Method method,
                    Object... params) {
                this.target = target;
                this.method = method;
                this.params = params;
                StringBuilder sb = new StringBuilder();
                sb.append(target.getClass().getName());
                sb.append(method.getName());
                for (Object obj : params) {
                    sb.append(obj.toString());
                }
                return sb.toString();
            }
        };
    }

    @Bean
    public RedisTemplate<String, String> redisTemplate(
            RedisConnectionFactory factory) {
        StringRedisTemplate template = new StringRedisTemplate(factory);
        // 使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值
        Jackson2JsonRedisSerializer<?> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(
                Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        // 使用StringRedisSerializer来序列化和反序列化redis的key值
        template.setValueSerializer(jackson2JsonRedisSerializer);
        template.afterPropertiesSet();
        return template;
    }

}
