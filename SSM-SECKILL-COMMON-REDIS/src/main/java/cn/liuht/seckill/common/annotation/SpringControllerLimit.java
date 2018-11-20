package cn.liuht.seckill.common.annotation;

import java.lang.annotation.*;

/**
 * 描述
 *
 * @author liuht
 * @date 2018/5/21 13:54
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SpringControllerLimit {

    /**
     * Error code
     * @return
     * code
     */
    int errorCode() default 500;

    /**
     * Error Message
     * @return
     * message
     */
    String errorMsg() default "request limited";
}
