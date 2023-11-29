package com.lcwd.electronic.store.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.api.ApiResponse;
import com.lcwd.electronic.store.services.CloudinaryImageService;

@RestController
@RequestMapping("/cloudinary/api")
public class CloudinaryImageController {

	@Autowired
	private CloudinaryImageService cloudinaryImageService;
	
	@PostMapping("/upload/image")
	public ResponseEntity<Map> uploadImage(@RequestParam("image") MultipartFile file) {
		Map data = cloudinaryImageService.uploadImage(file);
		return new ResponseEntity<Map>(data,HttpStatus.OK);
	}
	
	@DeleteMapping("/delete/{publicId}")
	public ResponseEntity<String> deleteImage(@PathVariable("publicId") String publicId){
		cloudinaryImageService.deleteImage(publicId);
		return new ResponseEntity<String>("Image Deleted Successfully",HttpStatus.OK);
	}
	
	@GetMapping("/image/{publicId}")
	public ResponseEntity<ApiResponse> getImage(@PathVariable("publicId") String publicId){
		ApiResponse response = cloudinaryImageService.getImage(publicId);
		return new ResponseEntity<ApiResponse>(response,HttpStatus.OK);
	}
}
