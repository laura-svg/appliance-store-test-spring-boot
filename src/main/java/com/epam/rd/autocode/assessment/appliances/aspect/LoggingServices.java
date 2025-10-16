package com.epam.rd.autocode.assessment.appliances.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingServices {
    private static final Logger logger = LoggerFactory.getLogger(LoggingServices.class);

    @Around("@annotation(com.epam.rd.autocode.assessment.appliances.aspect.Loggable)")
    public Object logExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().toShortString();
        Object[] args = joinPoint.getArgs();

        logger.info("Starting method: {}", methodName);
        if (args != null) {
            for (Object arg : args) {
                logger.debug("Argument: {}", arg);
            }
        }
        long startTime = System.currentTimeMillis();
        Object result;
        try {
            result = joinPoint.proceed();
        } catch (Exception ex) {
            logger.error("Exception in method: {}. Message: {}", methodName, ex.getMessage(), ex);
            throw ex;
        }
        long executionTime = System.currentTimeMillis() - startTime;
        logger.info("Method {} executed successfully in {} ms", methodName, executionTime);

        logger.debug("Result: {}", result);
        return result;
    }
}