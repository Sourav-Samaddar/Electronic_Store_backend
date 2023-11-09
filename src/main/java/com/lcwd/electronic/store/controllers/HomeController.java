package com.lcwd.electronic.store.controllers;

import org.aopalliance.aop.Advice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultSingletonBeanRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lcwd.electronic.store.config.ConfigurableAdvisorConfig;
import com.lcwd.electronic.store.config.MyAdvice;
import com.lcwd.electronic.store.properties.LoggerProperties;

@RestController
@RequestMapping("/test")
public class HomeController {
		
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
			
	@Autowired
	private LoggerProperties prop;

	@GetMapping
	public String testing() {
		logger.warn("Heloooooooooooo ddddddd");
		System.out.println("Helo my friend");
		return "I am in electronic store !!!!!!!!!";
	}
	
	@GetMapping("/value/{id}")
	public String testingAttribute(@PathVariable String id) {
		logger.warn("This is testingAttribute:"+id);
		System.out.println("Helo my friend");
		return "Testing attribute:"+id;
	}
	
	@GetMapping("/reinitializeBean")
	public String reinitializeBean() {
//	    DefaultSingletonBeanRegistry registry = (DefaultSingletonBeanRegistry) applicationContext.getAutowireCapableBeanFactory();
//	    AspectJExpressionPointcutAdvisor advisor =  
//	    		createAdvisor("execution(* com.lcwd.electronic.store.controllers.HomeController.*(..))",new MyAdvice());
//	    registry.destroySingleton("CustomAspectJExpression");
//	    registry.registerSingleton("CustomAspectJExpression", advisor);
	    
//		AnnotationConfigApplicationContext context =
//                new AnnotationConfigApplicationContext(ConfigurableAdvisorConfig.class);
//		ConfigurableBeanFactory beanFactory = context.getBeanFactory();
//				
//		AspectJExpressionPointcutAdvisor newBean = new AspectJExpressionPointcutAdvisor();
//		newBean.setExpression("execution(* com.lcwd.electronic.store.controllers.HomeController.*(..))");
//		newBean.setAdvice(new MyAdvice());
//		beanFactory.registerSingleton("CustomAspectJExpression1", newBean);
//		context.close();
		
//		GenericApplicationContext context = new GenericApplicationContext();
//		BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(ConfigurableAdvisorConfig.class);
//        BeanDefinition configBeanDefinition = builder.getBeanDefinition();
//        context.registerBeanDefinition("dynamicAspectConfiguration", configBeanDefinition);
//		
//		AspectJExpressionPointcutAdvisor newBean = new AspectJExpressionPointcutAdvisor();
//		newBean.setExpression("execution(* com.lcwd.electronic.store.controllers.HomeController.*(..))");
//		newBean.setAdvice(new MyAdvice());
//		context.registerBean("dynamicAdvisor", AspectJExpressionPointcutAdvisor.class, () -> newBean);
//        
//        BeanDefinition autoProxyCreatorDefinition = BeanDefinitionBuilder
//                .genericBeanDefinition(DefaultAdvisorAutoProxyCreator.class)
//                .getBeanDefinition();
//        context.registerBeanDefinition("defaultAdvisorAutoProxyCreator", autoProxyCreatorDefinition);
//
//        context.refresh();
//        
//        context.close();
		
		prop.save("putus");
		return prop.getAppserverlogPath();
	}
	
	@GetMapping("/loggerprop")
	public String getLoggerProp() {
		return prop.getPropertyKeyValue("application.name");
	}
	
	@GetMapping("/checkbean")
	public String checkBeanSey() {
		AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(ConfigurableAdvisorConfig.class);
		AspectJExpressionPointcutAdvisor bean = (AspectJExpressionPointcutAdvisor) context.getBean("CustomAspectJExpression");
		context.close();
		return bean.getExpression();
	}
	
	
	
}
