package com.lcwd.electronic.store.config;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.aspectj.lang.annotation.Aspect;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
public class MyAdvice implements MethodInterceptor {
	
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
    	if(invocation.getArguments().length > 0) {
    		log.debug("Before method !!!!!!:"+invocation.getMethod()+" "+invocation.getArguments()[0]);
    	}else {
    		log.debug("Before method name !!!!!!:"+invocation.getMethod());
    	}
        Object result = invocation.proceed();
        log.debug("After method !!!!!!!!:"+result);
        return result;
    }
}
