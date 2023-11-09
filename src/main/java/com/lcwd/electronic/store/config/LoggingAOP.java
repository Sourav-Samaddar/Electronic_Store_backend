package com.lcwd.electronic.store.config;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Slf4j
@Component
public class LoggingAOP {
	
	
	@Pointcut("execution(* com.lcwd.electronic.store.controllers.UserController.*(..))")
	public void loggingPointcut() {}
	
//	@Pointcut("within(com.lcwd.electronic.store.services..*)")
//	public void loggingPointcutWithin() {}
	
	@Before("loggingPointcut()")
	public void printBefore(JoinPoint joinPoint) {
		log.debug("Before method invoked:"+joinPoint.getSignature());
	}
	
//	@Before("loggingPointcutWithin()")
//	public void printBeforeWithin(JoinPoint joinPoint) {
//		log.debug("%% Before method invoked %%:"+joinPoint.getSignature());
//	}
	
	@After("loggingPointcut()")
	public void printAfter(JoinPoint joinPoint) {
		log.debug("After method invoked:"+joinPoint.getSignature());
	}
	
	@AfterReturning(value="loggingPointcut()",returning = "result")
	public void printAfterReturning(JoinPoint joinPoint,Object result) {
		if(result != null) {
			log.debug("After Returning method invoked:"+joinPoint.getSignature()+" "+result.toString());
		}
	}
	
	@AfterThrowing(value = "loggingPointcut()",
	            throwing = "e")
	public void printAfterThrowing( JoinPoint joinPoint, Exception e ){
		 log.error("After throwing method invoked::"+e.getMessage());
	}
 
	@Around("loggingPointcut()")
	public Object printAround(ProceedingJoinPoint joinPoint) throws Throwable{
		log.debug("Around Before method invoked::"+joinPoint.getArgs()[0]);
        Object object = joinPoint.proceed();
        log.debug("Around after method invoked::"+joinPoint.getArgs()[0]);
        return object;
	}
		
}
