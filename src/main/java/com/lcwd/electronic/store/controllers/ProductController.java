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
import com.lcwd.electronic.store.dtos.ImageResponse;
import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.dtos.ProductDto;
import com.lcwd.electronic.store.properties.ImageProperties;
import com.lcwd.electronic.store.services.FileService;
import com.lcwd.electronic.store.services.ProductService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/products")
public class ProductController {
	
	@Autowired
	ProductService productService;
	
	@Autowired
	FileService fileService;
	
	@Autowired
	private ImageProperties imageprop;

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/categories/{categoryId}")
	public ResponseEntity<ProductDto> createProduct(@Valid @RequestBody ProductDto productDto,
		 @PathVariable(name = "categoryId")	String categoryId) {
		return new ResponseEntity<ProductDto>(productService.
				createProduct(productDto, categoryId),HttpStatus.CREATED);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping
	public ResponseEntity<ProductDto> createProductWithoutCategory(@Valid @RequestBody ProductDto productDto) {
		return new ResponseEntity<ProductDto>(productService.
				createProductWithoutCaterory(productDto),HttpStatus.CREATED);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/{productId}")
	public ResponseEntity<ProductDto> updateProduct(@Valid @RequestBody ProductDto productDto,
			@PathVariable(name = "productId") String productId) {
		return new ResponseEntity<ProductDto>(productService.
				updateProduct(productDto, productId),HttpStatus.OK);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/{productId}/category/{categoryId}")
	public ResponseEntity<ProductDto> updateProductWithCategory(
			@PathVariable(name = "productId") String productId,
			@PathVariable(name = "categoryId") String categoryId) {
		return new ResponseEntity<ProductDto>(productService.
				updateProductWithCategory(productId, categoryId),HttpStatus.OK);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{productId}")
	public ResponseEntity<ApiResponseMessage> deleteProduct(@PathVariable(name = "productId") String productId){
		productService.deleteProduct(productId);
		return new ResponseEntity<ApiResponseMessage>(ApiResponseMessage
				.builder()
				.message("Product deleted successfully")
				.success(true)
				.status(HttpStatus.OK)
				.build(),HttpStatus.OK);
	}
	
	@GetMapping("/{productId}")
	public ResponseEntity<ProductDto> getProductById(@PathVariable(name = "productId") String productId) {
		return new ResponseEntity<ProductDto>(productService.getProductById(productId),HttpStatus.OK);
	}
	
	@GetMapping
	public ResponseEntity<PageableResponse<ProductDto>> getAllProducts(
			@RequestParam (value = "pageNumber",
			defaultValue = com.lcwd.electronic.store.config.AppConstant.PAGE_NUMBER,required = false) Integer pageNumber,
			@RequestParam (value = "pageSize",
			defaultValue = AppConstant.PAGE_SIZE,required = false) Integer pageSize,
			@RequestParam (value = "sortBy",
			defaultValue = AppConstant.SORT_BY_CATEGORY_TITLE,required = false) String sortBy,
			@RequestParam (value = "sortDirection",
			defaultValue = AppConstant.SORT_DIRECTION,required = false) String sortDirection){
				
		return new ResponseEntity<PageableResponse<ProductDto>>(productService.
				getAllProducts(pageNumber, pageSize, sortBy, sortDirection),HttpStatus.OK);
	}
	
	@GetMapping("/islive")
	public ResponseEntity<PageableResponse<ProductDto>> getAllLiveProducts(
			@RequestParam (value = "pageNumber",
			defaultValue = com.lcwd.electronic.store.config.AppConstant.PAGE_NUMBER,required = false) Integer pageNumber,
			@RequestParam (value = "pageSize",
			defaultValue = AppConstant.PAGE_SIZE,required = false) Integer pageSize,
			@RequestParam (value = "sortBy",
			defaultValue = AppConstant.SORT_BY_CATEGORY_TITLE,required = false) String sortBy,
			@RequestParam (value = "sortDirection",
			defaultValue = AppConstant.SORT_DIRECTION,required = false) String sortDirection){
				
		return new ResponseEntity<PageableResponse<ProductDto>>(productService.
				getAllLiveProducts(pageNumber, pageSize, sortBy, sortDirection),HttpStatus.OK);
	}
	
	@GetMapping("/instock")
	public ResponseEntity<PageableResponse<ProductDto>> getAllProductsInStock(
			@RequestParam (value = "pageNumber",
			defaultValue = com.lcwd.electronic.store.config.AppConstant.PAGE_NUMBER,required = false) Integer pageNumber,
			@RequestParam (value = "pageSize",
			defaultValue = AppConstant.PAGE_SIZE,required = false) Integer pageSize,
			@RequestParam (value = "sortBy",
			defaultValue = AppConstant.SORT_BY_CATEGORY_TITLE,required = false) String sortBy,
			@RequestParam (value = "sortDirection",
			defaultValue = AppConstant.SORT_DIRECTION,required = false) String sortDirection){
				
		return new ResponseEntity<PageableResponse<ProductDto>>(productService.
				getAllProductsInStock(pageNumber, pageSize, sortBy, sortDirection),HttpStatus.OK);
	}
	
	@GetMapping("/searchbytitle/{subTitle}")
	public ResponseEntity<PageableResponse<ProductDto>> getProductsBySubTitle(
			@PathVariable(name = "subTitle") String subTitle,
			@RequestParam (value = "pageNumber",
			defaultValue = com.lcwd.electronic.store.config.AppConstant.PAGE_NUMBER,required = false) Integer pageNumber,
			@RequestParam (value = "pageSize",
			defaultValue = AppConstant.PAGE_SIZE,required = false) Integer pageSize,
			@RequestParam (value = "sortBy",
			defaultValue = AppConstant.SORT_BY_CATEGORY_TITLE,required = false) String sortBy,
			@RequestParam (value = "sortDirection",
			defaultValue = AppConstant.SORT_DIRECTION,required = false) String sortDirection){
				
		return new ResponseEntity<PageableResponse<ProductDto>>(productService.
				getProductsBySubTitle(subTitle, pageNumber, pageSize, sortBy, sortDirection),HttpStatus.OK);
	}
	
	@GetMapping("/live/searchbytitle/{subTitle}")
	public ResponseEntity<PageableResponse<ProductDto>> getLiveProductsBySubTitle(
			@PathVariable(name = "subTitle") String subTitle,
			@RequestParam (value = "pageNumber",
			defaultValue = com.lcwd.electronic.store.config.AppConstant.PAGE_NUMBER,required = false) Integer pageNumber,
			@RequestParam (value = "pageSize",
			defaultValue = AppConstant.PAGE_SIZE,required = false) Integer pageSize,
			@RequestParam (value = "sortBy",
			defaultValue = AppConstant.SORT_BY_CATEGORY_TITLE,required = false) String sortBy,
			@RequestParam (value = "sortDirection",
			defaultValue = AppConstant.SORT_DIRECTION,required = false) String sortDirection){
				
		return new ResponseEntity<PageableResponse<ProductDto>>(productService.
				getLiveProductsBySubTitle(subTitle, pageNumber, pageSize, sortBy, sortDirection),HttpStatus.OK);
	}
	
	@GetMapping("/searchbycategory/{categoryId}")
	public ResponseEntity<PageableResponse<ProductDto>> getProductsByCategory(
			@PathVariable(name = "categoryId") String categoryId,
			@RequestParam (value = "pageNumber",
			defaultValue = com.lcwd.electronic.store.config.AppConstant.PAGE_NUMBER,required = false) Integer pageNumber,
			@RequestParam (value = "pageSize",
			defaultValue = AppConstant.PAGE_SIZE,required = false) Integer pageSize,
			@RequestParam (value = "sortBy",
			defaultValue = AppConstant.SORT_BY_CATEGORY_TITLE,required = false) String sortBy,
			@RequestParam (value = "sortDirection",
			defaultValue = AppConstant.SORT_DIRECTION,required = false) String sortDirection){
				
		return new ResponseEntity<PageableResponse<ProductDto>>(productService.
				getProductsByCategory(categoryId, pageNumber, pageSize, sortBy, sortDirection),HttpStatus.OK);
	}
	
	@GetMapping("/live/searchbycategory/{categoryId}")
	public ResponseEntity<PageableResponse<ProductDto>> getLiveProductsByCategory(
			@PathVariable(name = "categoryId") String categoryId,
			@RequestParam (value = "pageNumber",
			defaultValue = com.lcwd.electronic.store.config.AppConstant.PAGE_NUMBER,required = false) Integer pageNumber,
			@RequestParam (value = "pageSize",
			defaultValue = AppConstant.PAGE_SIZE,required = false) Integer pageSize,
			@RequestParam (value = "sortBy",
			defaultValue = AppConstant.SORT_BY_CATEGORY_TITLE,required = false) String sortBy,
			@RequestParam (value = "sortDirection",
			defaultValue = AppConstant.SORT_DIRECTION,required = false) String sortDirection){
				
		return new ResponseEntity<PageableResponse<ProductDto>>(productService.
				getLiveProductsByCategory(categoryId, pageNumber, pageSize, sortBy, sortDirection),HttpStatus.OK);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/image/{productId}")
	public ResponseEntity<ImageResponse> uploadProductImage(@PathVariable(name = "productId") String productId,
			@RequestParam(name = "productImage") MultipartFile image) throws IOException{
		String imageFileName = fileService.uploadFile(image, imageprop.getProductImagePath());
		ProductDto productDto = productService.getProductById(productId);
		String oldImageToDelete = productDto.getProductImageName();
		//System.out.println("*********oldImageToDelete:"+oldImageToDelete);
		if(oldImageToDelete != null && oldImageToDelete.length() > 0) {
			String fullPath = imageprop.getProductImagePath() + oldImageToDelete;
			Path path = Paths.get(fullPath);
			try {
				Files.deleteIfExists(path);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		productDto.setProductImageName(imageFileName);
		productDto = productService.updateProduct(productDto, productId);
		return new ResponseEntity<ImageResponse>(ImageResponse
				.builder()
				.imageName(imageFileName)
				.message("Product image uploaded successfully")
				.success(true)
				.status(HttpStatus.OK)
				.build(),HttpStatus.OK);
	}
	
	@GetMapping(value = "/image/{productId}", produces = MediaType.IMAGE_JPEG_VALUE)
	public void downloadProductImage(@PathVariable(name = "productId") String productId,
			HttpServletResponse response) throws IOException {
		ProductDto productDto = productService.getProductById(productId);
		String imageName = productDto.getProductImageName();
		if(imageName != null && imageName.length()>0) {
			InputStream inputStream = fileService.getResource(imageprop.getProductImagePath(), imageName);
			response.setContentType(MediaType.IMAGE_JPEG_VALUE);
			StreamUtils.copy(inputStream, response.getOutputStream());
			inputStream.close();
		}
	}
}
