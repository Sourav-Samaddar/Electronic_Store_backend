package com.lcwd.electronic.store.services;

import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.api.ApiResponse;

public interface CloudinaryImageService {

	public Map uploadImage(MultipartFile file);
	
	public void deleteImage(String assetId);
	
	public ApiResponse getImage(String assetId);
}
