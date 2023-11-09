package com.lcwd.electronic.store.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@PropertySource("classpath:/customprop/imageconfig.properties")
@Component
@Getter
@Setter
@ToString
public class ImageProperties {
	@Value("${user.profile.image.path}")
	private String userImagePath;
	
	@Value("${categories.profile.image.path}")
	private String categoryImagePath;
	
	@Value("${products.profile.image.path}")
	private String productImagePath;
}
