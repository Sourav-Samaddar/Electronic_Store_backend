package com.lcwd.electronic.store.properties;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


//@ConfigurationProperties(prefix = "lcw.config")
@PropertySource("classpath:/customprop/loggerconfig.properties")
@Component
@Getter
@Setter
@ToString
public class LoggerProperties {
	
	@Value("classpath:log4j2.xml")
	private Resource resourceLog4j;
    
	@Value("${application.file.path.appserver}")
    private String appserverlogPath;
	
	@Value("${pointcut.property}")
	private String pointcutprop;
	
	@Value("${pointcut.within}")
	private String pointcutwithin;
	
	@Value("classpath:customprop/loggerconfig.properties")
	private Resource loggerpropFileResource;
	
	public void save(String newOffset) {
        try {
        	Properties props = null;
        	try (FileInputStream in = new FileInputStream(loggerpropFileResource.getFile())) {
        	    props = new Properties();
        	    props.load(in);
        	}
        	        
        	try (FileOutputStream out = new FileOutputStream(loggerpropFileResource.getFile())){
        	    props.setProperty("application.name", newOffset);
        	    props.store(out, null);
        	}
        } catch (Exception e ) {
            e.printStackTrace();
        }
    }
	
	public String getPropertyKeyValue(String key) {
		Properties props = null;
		try {
        	
        	try (FileInputStream in = new FileInputStream(loggerpropFileResource.getFile())) {
        	    props = new Properties();
        	    props.load(in);
        	}
        	
        } catch (Exception e ) {
            e.printStackTrace();
        }
		return props.getProperty(key);
	}
}
