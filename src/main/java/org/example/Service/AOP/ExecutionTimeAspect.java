package org.example.Service.AOP;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class ExecutionTimeAspect {
    @Around("@annotation(org.example.Service.AOP.LogExecutionTime)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable{
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getMethod().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        Object[] args = joinPoint.getArgs();

        String params = args.length > 0 ? Arrays.toString(args) : "без параметров";

        log.info("{}.{} вызван. Параметры: {}", className, methodName, params);

        long startTime = System.currentTimeMillis();

        try{
            Object result = joinPoint.proceed();
            long duration = System.currentTimeMillis() - startTime;
            log.info("{}.{} завершен за {}мс. Результат: {}",
                    className, methodName, duration,
                    result != null ? result : "void");
            return result;
        }catch (Exception e){
            long duration = System.currentTimeMillis() - startTime;
            log.error("{}.{} упал через {}мс. Ошибка: {}",
                    className, methodName, duration, e.getMessage(), e);
            throw e;
        }
    }
}
