package com.estatesoft.ris.wx.interceptor;

import com.alibaba.fastjson.JSON;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

@Aspect
@Component
public class LogInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(LogInterceptor.class);

    public LogInterceptor() {
    }

    @Pointcut("@annotation(Log)")
    private void logAspect() {
    }

    @Around("logAspect()")
    public Object log(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result;
        if (logger.isInfoEnabled()) {
            MethodSignature signature = (MethodSignature)joinPoint.getSignature();
            Method method = signature.getMethod();
            Annotation[] declareAnnotations = method.getDeclaredAnnotations();
            Log description = null;
            Annotation[] var7 = declareAnnotations;
            int var8 = declareAnnotations.length;

            for(int var9 = 0; var9 < var8; ++var9) {
                Annotation annotation = var7[var9];
                if (annotation instanceof Log) {
                    description = (Log)annotation;
                    Object[] args = joinPoint.getArgs();
                    logger.debug("=====>>>" + description.value()[0] + ",方法名:{},传入参数:{}", method.getName(), JSON.toJSONString(args));
                }
            }

            result = joinPoint.proceed();
            logger.debug("=====>>>" + description.value()[0] + ",方法名:{},返回数据:{}", method.getName(), result == null ? null : JSON.toJSONString(result));
        } else {
            result = joinPoint.proceed();
        }

        return result;
    }
}

