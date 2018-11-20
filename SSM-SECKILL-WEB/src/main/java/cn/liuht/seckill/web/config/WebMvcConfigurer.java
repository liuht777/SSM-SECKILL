package cn.liuht.seckill.web.config;

import cn.liuht.seckill.common.RedisLimit;
import cn.liuht.seckill.common.intercept.SpringMVCIntercept;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * mvc 配置
 *
 * @author liuht
 * @date 2018/5/21 14:11
 */
@Configuration
public class WebMvcConfigurer extends WebMvcConfigurerAdapter {

    @Autowired
    private RedisLimit redisLimit;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SpringMVCIntercept(redisLimit));
        super.addInterceptors(registry);
    }
}
