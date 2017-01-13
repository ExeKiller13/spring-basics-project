package com.alokhin.spring.core.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
public class StatisticsAspect {

    private Map<Class<?>, Integer> counter = new HashMap<>();

    @Pointcut("execution(* *.logEvent(..))")
    private void allLogEventMethods() {
    }

    @AfterReturning("allLogEventMethods()")
    public void logBefore(JoinPoint joinPoint) {
        Class<?> ourClass = joinPoint.getTarget().getClass();
        if (!counter.containsKey(ourClass)) {
            counter.put(ourClass, 0);
        }
        counter.put(ourClass, counter.get(ourClass) + 1);
    }

    public Map<Class<?>, Integer> getCounter() {
        return counter;
    }
}
