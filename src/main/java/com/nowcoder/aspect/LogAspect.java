package com.nowcoder.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by nowcoder on 2016/7/10.
 */
@Aspect
@Component
public class LogAspect {
    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);
    private static final SimpleDateFormat mydate = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");

    @Before("execution(* com.nowcoder.controller.*Controller.*(..))")
    public void beforeMethod(JoinPoint joinPoint) {
        logger.info("before method: " + mydate.format(new Date()));
    }

    @After("execution(* com.nowcoder.controller.*Controller.*(..))")
    public void afterMethod() {
        logger.info("after method： " + mydate.format(new Date()));
    }
}
