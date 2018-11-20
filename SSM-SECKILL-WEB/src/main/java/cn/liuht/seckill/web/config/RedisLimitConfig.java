package cn.liuht.seckill.web.config;

import cn.liuht.seckill.common.RedisLimit;
import cn.liuht.seckill.common.constant.RedisToolsConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

/**
 * 描述
 *
 * @author liuht
 * @date 2018/5/21 11:39
 */
@Configuration
public class RedisLimitConfig {

    @Value("${redis.limit}")
    private int limit;


    @Autowired
    private JedisConnectionFactory jedisConnectionFactory;

    @Bean
    public RedisLimit build() {
        return new RedisLimit.Builder(jedisConnectionFactory, RedisToolsConstant.SINGLE)
                .limit(limit)
                .build();
    }
}
