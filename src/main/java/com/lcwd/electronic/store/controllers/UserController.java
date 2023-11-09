package com.lcwd.electronic.store.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


import com.lcwd.electronic.store.config.AppConstant;
import com.lcwd.electronic.store.dtos.ImageResponse;
import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.dtos.UserDto;
import com.lcwd.electronic.store.properties.ImageProperties;
import com.lcwd.electronic.store.services.FileService;
import com.lcwd.electronic.store.services.UserService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private FileService fileService;
	
	@Autowired
	private ImageProperties imageprop;
	
	@PostMapping
	public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto) {
		return new ResponseEntity<UserDto>(userService.createUser(userDto),HttpStatus.CREATED);
	}

	@PutMapping("/{userId}")
	public ResponseEntity<UserDto> updateUser(@Valid @RequestBody UserDto userDto,
			@PathVariable(name = "userId") String userId){
		return new ResponseEntity<UserDto>(userService.updateUser(userDto,userId),HttpStatus.OK);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{userId}")
	public ResponseEntity<String> deleteUser(@PathVariable(name = "userId") String userId){
		userService.deleteUser(userId);
		return new ResponseEntity<String>("User deleted successfully",HttpStatus.OK);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping
	public ResponseEntity<PageableResponse<UserDto>> getAllUsers(
			@RequestParam (value = "pageNumber",
			defaultValue = com.lcwd.electronic.store.config.AppConstant.PAGE_NUMBER,required = false) Integer pageNumber,
			@RequestParam (value = "pageSize",
			defaultValue = AppConstant.PAGE_SIZE,required = false) Integer pageSize,
			@RequestParam (value = "sortBy",
			defaultValue = AppConstant.SORT_BY_USERNAME,required = false) String sortBy,
			@RequestParam (value = "sortDirection",
			defaultValue = AppConstant.SORT_DIRECTION,required = false) String sortDirection){
		return new ResponseEntity<PageableResponse<UserDto>>(userService.
				getAllUsers(pageNumber,pageSize,sortBy,sortDirection),HttpStatus.OK);
	}
	
	@GetMapping("/{userId}")
	public ResponseEntity<UserDto> getUserById(@PathVariable(name = "userId") String userId){
		return new ResponseEntity<UserDto>(userService.getUser(userId),HttpStatus.OK);
	}
	
	@GetMapping("/email/{emailId}")
	public ResponseEntity<UserDto> getUserByEmail(@PathVariable(name = "emailId") String emailId){
		return new ResponseEntity<UserDto>(userService.getUserByEmail(emailId),HttpStatus.OK);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/search/{keyword}")
	public ResponseEntity<PageableResponse<UserDto>> searchUser(@PathVariable(name = "keyword") String keyword,
			@RequestParam (value = "pageNumber",
			defaultValue = com.lcwd.electronic.store.config.AppConstant.PAGE_NUMBER,required = false) Integer pageNumber,
			@RequestParam (value = "pageSize",
			defaultValue = AppConstant.PAGE_SIZE,required = false) Integer pageSize,
			@RequestParam (value = "sortBy",
			defaultValue = AppConstant.SORT_BY_USERNAME,required = false) String sortBy,
			@RequestParam (value = "sortDirection",
			defaultValue = AppConstant.SORT_DIRECTION,required = false) String sortDirection){
		return new ResponseEntity<PageableResponse<UserDto>>(userService.
				searchUser(keyword,pageNumber,pageSize,sortBy,sortDirection),HttpStatus.OK);
	}
	
	@PostMapping("/image/{userId}")
	public ResponseEntity<ImageResponse> uploadUserImage(@PathVariable(name = "userId") String userId,
			@RequestParam(name = "userImage") MultipartFile image) throws IOException {
		String imageFileName = fileService.uploadFile(image, imageprop.getUserImagePath());
		UserDto userDto = userService.getUser(userId);
		String oldImageToDelete = userDto.getImageName();
		userDto.setImageName(imageFileName);
		userDto = userService.updateUser(userDto, userId);
		if(oldImageToDelete != null && oldImageToDelete.length() > 0 && 
				!oldImageToDelete.equalsIgnoreCase("default.png")) {
			String fullPath = imageprop.getUserImagePath() + oldImageToDelete;
			Path path = Paths.get(fullPath);
			try {
				Files.deleteIfExists(path);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return new ResponseEntity<ImageResponse>(ImageResponse.builder()
				.imageName(imageFileName)
				.message("Image Uploaded successfully")
				.success(true)
				.status(HttpStatus.OK)
				.build(),HttpStatus.OK);
	}
	
	@GetMapping(value="/image/{userId}", produces = MediaType.IMAGE_JPEG_VALUE)
	public void downloadImage(@PathVariable(name = "userId") String userId,
			HttpServletResponse response) throws IOException{
		UserDto userDto = userService.getUser(userId);
		String imageName = userDto.getImageName();
		if(imageName != null && imageName.length()>0 && !imageName.equalsIgnoreCase("default.png")) {
			InputStream inputStream = fileService.getResource(imageprop.getUserImagePath(), imageName);
			response.setContentType(MediaType.IMAGE_JPEG_VALUE);
			StreamUtils.copy(inputStream, response.getOutputStream());
			inputStream.close();
		}
	}
}
