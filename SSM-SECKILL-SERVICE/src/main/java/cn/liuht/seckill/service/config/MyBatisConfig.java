package cn.liuht.seckill.service.config;

import com.github.pagehelper.PageInterceptor;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.boot.autoconfigure.MybatisProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * mybatis 配置类
 *
 * @author liuht
 * @date 2017/10/11 13:45
 */
@Configuration
@EnableTransactionManagement
@ConditionalOnBean(DataSource.class)
public class MyBatisConfig {
    private static final Logger logger = LoggerFactory.getLogger(MyBatisConfig.class);

    @Autowired
    private DataSource dataSource;

    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactoryBean() {
        final SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);

        MybatisInterceptor performanceInterceptor = new MybatisInterceptor();
        PageInterceptor pageInterceptor = new PageInterceptor();
        Properties props = new Properties();
        // 设置为true时，使用RowBounds分页会进行count查询
        props.setProperty("rowBoundsWithCount", "true");
        // 设置为true时，如果pageSize=0或者RowBounds.limit = 0就会查询出全部的结果
        // 相当于没有执行分页查询，但是返回结果仍然是Page类型
        props.setProperty("pageSizeZero", "true");
        pageInterceptor.setProperties(props);
        //添加插件
        Interceptor[] interceptor = new Interceptor[]{pageInterceptor, performanceInterceptor};
        sqlSessionFactoryBean.setPlugins(interceptor);
        try {
            ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            sqlSessionFactoryBean.setMapperLocations(resolver.getResources("classpath:**/*Mapper.xml"));
            return sqlSessionFactoryBean.getObject();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }


    /**
     * 注入 SqlSessionTemplate
     *
     * @return SqlSessionTemplate
     */
    @Bean
    @ConfigurationProperties(prefix = MybatisProperties.MYBATIS_PREFIX)
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory, MybatisProperties properties) {
        ExecutorType executorType = properties.getExecutorType();
        if (executorType != null) {
            return new SqlSessionTemplate(sqlSessionFactory, ExecutorType.SIMPLE);
        } else {
            return new SqlSessionTemplate(sqlSessionFactory);
        }
    }

}
