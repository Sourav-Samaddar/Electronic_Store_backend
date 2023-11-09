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
import com.lcwd.electronic.store.dtos.ApiResponseMessage;
import com.lcwd.electronic.store.dtos.CategoryDto;
import com.lcwd.electronic.store.dtos.ImageResponse;
import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.properties.ImageProperties;
import com.lcwd.electronic.store.services.CategoryService;
import com.lcwd.electronic.store.services.FileService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/categories")
public class CategoryController {
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private FileService fileService;
	
	@Autowired
	private ImageProperties imageprop;

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping
	public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto categoryDto) {
		return new ResponseEntity<CategoryDto>(categoryService.createCategory(categoryDto),HttpStatus.CREATED);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/{categoryId}")
	public ResponseEntity<CategoryDto> updateCategory(@Valid @RequestBody CategoryDto categoryDto,
			@PathVariable(name = "categoryId") String categoryId) {
		return new ResponseEntity<CategoryDto>(categoryService.
				updateCategory(categoryDto,categoryId),HttpStatus.CREATED);
	} 
	
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{categoryId}")
	public ResponseEntity<ApiResponseMessage> deleteCategory(@PathVariable(name = "categoryId") String categoryId){
		categoryService.deleteCategory(categoryId);
		return new ResponseEntity<ApiResponseMessage>(ApiResponseMessage
				.builder()
				.message("Category deleted successfully")
				.success(true)
				.status(HttpStatus.OK)
				.build(),HttpStatus.OK);
	}
	
	@GetMapping("/{categoryId}")
	public ResponseEntity<CategoryDto> getCategoryById(@PathVariable(name = "categoryId") String categoryId) {
		return new ResponseEntity<CategoryDto>(categoryService.getCategoryById(categoryId),HttpStatus.OK);
	}
	
	@GetMapping
	public ResponseEntity<PageableResponse<CategoryDto>> getAllCategories(
			@RequestParam (value = "pageNumber",
			defaultValue = com.lcwd.electronic.store.config.AppConstant.PAGE_NUMBER,required = false) Integer pageNumber,
			@RequestParam (value = "pageSize",
			defaultValue = AppConstant.PAGE_SIZE,required = false) Integer pageSize,
			@RequestParam (value = "sortBy",
			defaultValue = AppConstant.SORT_BY_CATEGORY_TITLE,required = false) String sortBy,
			@RequestParam (value = "sortDirection",
			defaultValue = AppConstant.SORT_DIRECTION,required = false) String sortDirection){
		return new ResponseEntity<PageableResponse<CategoryDto>>(categoryService.
				getAllCategories(pageNumber, pageSize, sortBy, sortDirection),HttpStatus.OK);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/image/{categoryId}")
	public ResponseEntity<ImageResponse> uploadCategoryImage(@PathVariable(name = "categoryId") String categoryId,
			@RequestParam(name = "categoryImage") MultipartFile image) throws IOException{
		String imageFileName = fileService.uploadFile(image, imageprop.getCategoryImagePath());
		CategoryDto categoryDto = categoryService.getCategoryById(categoryId);
		String oldImageToDelete = categoryDto.getCoverImage();
		
		if(oldImageToDelete != null && oldImageToDelete.length() > 0) {
			String fullPath = imageprop.getCategoryImagePath() + oldImageToDelete;
			Path path = Paths.get(fullPath);
			try {
				Files.deleteIfExists(path);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		categoryDto.setCoverImage(imageFileName);
		categoryDto = categoryService.updateCategory(categoryDto, categoryId);
		return new ResponseEntity<ImageResponse>(ImageResponse
				.builder()
				.imageName(imageFileName)
				.message("Category image uploaded successfully")
				.success(true)
				.status(HttpStatus.OK)
				.build(),HttpStatus.OK);
	}
	
	@GetMapping(value = "/image/{categoryId}", produces = MediaType.IMAGE_JPEG_VALUE)
	public void downloadCategoryImage(@PathVariable(name = "categoryId") String categoryId,
			HttpServletResponse response) throws IOException {
		CategoryDto categoryDto = categoryService.getCategoryById(categoryId);
		String imageName = categoryDto.getCoverImage();
		if(imageName != null && imageName.length()>0) {
			InputStream inputStream = fileService.getResource(imageprop.getCategoryImagePath(), imageName);
			response.setContentType(MediaType.IMAGE_JPEG_VALUE);
			StreamUtils.copy(inputStream, response.getOutputStream());
			inputStream.close();
		}
	}
}
