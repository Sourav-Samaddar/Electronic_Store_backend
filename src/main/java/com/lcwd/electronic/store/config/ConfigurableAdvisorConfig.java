package com.lcwd.electronic.store.config;

import org.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.lcwd.electronic.store.properties.LoggerProperties;

@Configuration
public class ConfigurableAdvisorConfig {
    
    @Autowired
    private LoggerProperties prop;
      
    @Bean(name = "CustomAspectJExpression")
    public AspectJExpressionPointcutAdvisor myconfigurabledvisor() {
        AspectJExpressionPointcutAdvisor advisor = new AspectJExpressionPointcutAdvisor();
        advisor.setExpression(prop.getPointcutprop());
        advisor.setAdvice(new MyAdvice());
        return advisor;
    }
       
//    @Bean(name = "CustomAspectJExpression1")
//    public AspectJExpressionPointcutAdvisor ownconfigurabledvisor() {
//        AspectJExpressionPointcutAdvisor advisor = new AspectJExpressionPointcutAdvisor();
//        advisor.setExpression("execution(* com.lcwd.electronic.store.controllers.HomeController.*(..))");
//        advisor.setAdvice(new MyAdvice());
//        return advisor;
//    }
}
