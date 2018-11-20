package cn.liuht.seckill.service;

import com.alibaba.dubbo.spring.boot.annotation.EnableDubboConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * 描述
 *
 * @author liuht
 * @date 2018/5/21 11:07
 */
@SpringBootApplication
@MapperScan(basePackages = "cn.liuht.seckill.service.mapper")
@EnableDubboConfiguration
public class OrderProviderApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(OrderProviderApplication.class)
                // .web(false)
                .run(args);
    }
}
