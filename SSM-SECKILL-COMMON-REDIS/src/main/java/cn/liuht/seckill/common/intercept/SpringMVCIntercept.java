package cn.liuht.seckill.common.intercept;

import cn.liuht.seckill.common.RedisLimit;
import cn.liuht.seckill.common.annotation.SpringControllerLimit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * RedisLimit SpringMVC拦截器
 * 借助于SpringControllerLimit实现
 *
 * @author liuht
 * @date 2018/5/21 13:57
 */
public class SpringMVCIntercept extends HandlerInterceptorAdapter {
    private static Logger logger = LoggerFactory.getLogger(SpringMVCIntercept.class);

    private RedisLimit redisLimit;

    public SpringMVCIntercept(RedisLimit redisLimit) {
        this.redisLimit = redisLimit;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (redisLimit == null) {
            throw new NullPointerException("redisLimit is null");
        }
        if (handler instanceof HandlerMethod) {
            HandlerMethod method = (HandlerMethod) handler;
            SpringControllerLimit annotation = method.getMethodAnnotation(SpringControllerLimit.class);
            if (annotation == null) {
                //skip
                return true;
            }
            boolean limit = redisLimit.limit();
            if (!limit) {
                logger.warn(annotation.errorMsg());
                response.sendError(annotation.errorCode(), annotation.errorMsg());
                return false;
            }
        }

        return true;

    }
}
