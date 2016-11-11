package com.ote.test.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicLong;

@Aspect
public class TraceableAspect {

    private static final AtomicLong COUNT = new AtomicLong(0);

    @Around("execution(* *(..)) && @annotation(com.ote.test.aop.Traceable)")
    public Object execute(ProceedingJoinPoint point) throws Throwable {

        final Logger logger = LoggerFactory.getLogger(point.getTarget().getClass());

        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        Traceable annotation = method.getAnnotation(Traceable.class);

        if (annotation.level().isEnabled(logger)) {
            return executeWithTrace(point, logger, annotation.level());
        } else {
            return point.proceed();
        }
    }

    private Object executeWithTrace(ProceedingJoinPoint point, Logger logger, Traceable.Level level) throws Throwable {
        long start = System.currentTimeMillis();
        long count = COUNT.getAndIncrement();
        String methodName = point.getSignature().getName();

        try {
            level.log(logger, String.format("####-%d-START- %s", count, methodName));
            return point.proceed();
        } catch (Throwable e) {
            level.log(logger, String.format("####-%d-ERROR- %s", count, methodName), e);
            throw e;
        } finally {
            long end = System.currentTimeMillis();
            level.log(logger, String.format("####-%d-END  - %s in %sms", count, methodName, (end - start)));
        }
    }
}