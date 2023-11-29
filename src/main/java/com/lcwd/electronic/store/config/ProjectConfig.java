package com.lcwd.electronic.store.config;

import java.util.HashMap;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

@Configuration
public class ProjectConfig {

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
	
	@Bean
	public Cloudinary getCloudinary() {
		
		Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
				  "cloud_name", "ds5pxhqlg",
				  "api_key", "627759727651657",
				  "api_secret", "l86zTJY9C6d-bOiG9vfohDpV1Qg",
				  "secure",true));
		return cloudinary;
	}
}
