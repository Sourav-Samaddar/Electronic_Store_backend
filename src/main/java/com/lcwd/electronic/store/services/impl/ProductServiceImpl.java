package com.lcwd.electronic.store.services.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.lcwd.electronic.store.dtos.CategoryDto;
import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.dtos.ProductDto;
import com.lcwd.electronic.store.entities.Category;
import com.lcwd.electronic.store.entities.Product;
import com.lcwd.electronic.store.exceptions.ResourceNotFoundException;
import com.lcwd.electronic.store.helper.Helper;
import com.lcwd.electronic.store.repositories.CategoryRepository;
import com.lcwd.electronic.store.repositories.ProductRepository;
import com.lcwd.electronic.store.services.ProductService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService{
	
	@Autowired
	ProductRepository productRepository;
	
	@Autowired
	CategoryRepository categoryRepository;
	
	@Autowired
	ModelMapper modelMapper;
	
	@Value("${products.profile.image.path}")
	private String imagePath;

	@Override
	public ProductDto createProduct(ProductDto productDto, String categoryId) {
		Category category = categoryRepository.findById(categoryId).
				orElseThrow(() -> new ResourceNotFoundException("Category Id not available"));
		String productId = UUID.randomUUID().toString();
		productDto.setProductId(productId);
		productDto.setAddedDate(new Date());
		productDto.setCategory(modelMapper.map(category, CategoryDto.class));
		Product savedProduct = productRepository.save(modelMapper.map(productDto, Product.class));
		
		return modelMapper.map(savedProduct, ProductDto.class);
	}

	@Override
	public ProductDto updateProduct(ProductDto productDto, String productId) {
		Product product = productRepository.findById(productId).
				orElseThrow(() -> new ResourceNotFoundException("Product Id not available"));
		
		product.setTitle(productDto.getTitle());
		product.setShortName(productDto.getShortName());
		product.setDescription(productDto.getDescription());
		product.setPrice(productDto.getPrice());
		product.setDiscountedPrice(productDto.getDiscountedPrice());
		product.setLive(productDto.isLive());
		product.setStock(productDto.isStock());
		product.setQuantity(productDto.getQuantity());
		//System.out.println("********getProductImageName:"+productDto.getProductImageName());
		product.setProductImageName(productDto.getProductImageName());
		Product savedProduct = productRepository.save(product);
		
		return modelMapper.map(savedProduct, ProductDto.class);
	}

	@Override
	public void deleteProduct(String productId) {
		Product product = productRepository.findById(productId).
				orElseThrow(() -> new ResourceNotFoundException("Product Id not available"));
		String fullPath = imagePath + product.getProductImageName();
		Path path = Paths.get(fullPath);
		try {
			Files.deleteIfExists(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
		productRepository.delete(product);
	}

	@Override
	public PageableResponse<ProductDto> getAllProducts(Integer pageNumber, Integer pageSize, String sortBy,
			String sortDirection) {
		Sort sort = Helper.getSorting(sortBy, sortDirection);
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
		Page<Product> pageProduct = productRepository.findAll(pageable);
		PageableResponse<ProductDto> response = Helper.getPageableResponse(pageProduct, ProductDto.class);
		return response;
	}

	@Override
	public PageableResponse<ProductDto> getAllLiveProducts(Integer pageNumber, Integer pageSize, String sortBy,
			String sortDirection) {
		Sort sort = Helper.getSorting(sortBy, sortDirection);
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
		Page<Product> pageProduct = productRepository.findByLiveTrue(pageable);
		PageableResponse<ProductDto> response = Helper.getPageableResponse(pageProduct, ProductDto.class);
		log.debug("*** Inside getAllLiveProducts ***");
		return response;
	}

	@Override
	public PageableResponse<ProductDto> getAllProductsInStock(Integer pageNumber, Integer pageSize, String sortBy,
			String sortDirection) {
		Sort sort = Helper.getSorting(sortBy, sortDirection);
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
		Page<Product> pageProduct = productRepository.findByStockTrue(pageable);
		PageableResponse<ProductDto> response = Helper.getPageableResponse(pageProduct, ProductDto.class);
		return response;
	}

	@Override
	public PageableResponse<ProductDto> getProductsBySubTitle(String subTitle, Integer pageNumber, Integer pageSize,
			String sortBy, String sortDirection) {
		Sort sort = Helper.getSorting(sortBy, sortDirection);
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
		Page<Product> pageProduct = productRepository.findByTitleContaining(subTitle, pageable);
		PageableResponse<ProductDto> response = Helper.getPageableResponse(pageProduct, ProductDto.class);
		return response;
	}

	@Override
	public ProductDto getProductById(String productId) {
		Product product = productRepository.findById(productId).
				orElseThrow(() -> new ResourceNotFoundException("Product Id not available"));
		return modelMapper.map(product, ProductDto.class);
	}

	@Override
	public PageableResponse<ProductDto> getProductsByCategory(String categoryId, Integer pageNumber, Integer pageSize,
			String sortBy, String sortDirection) {
		Category category = categoryRepository.findById(categoryId).
				orElseThrow(() -> new ResourceNotFoundException("Category Id not available"));
		Sort sort = Helper.getSorting(sortBy, sortDirection);
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
		Page<Product> pageProduct = productRepository.findByCategory(category, pageable);
		PageableResponse<ProductDto> response = Helper.getPageableResponse(pageProduct, ProductDto.class);
		return response;
	}

	@Override
	public ProductDto createProductWithoutCaterory(ProductDto productDto) {
		
		String productId = UUID.randomUUID().toString();
		productDto.setProductId(productId);
		productDto.setAddedDate(new Date());
		Product savedProduct = productRepository.save(modelMapper.map(productDto, Product.class));
		
		return modelMapper.map(savedProduct, ProductDto.class);
	}

	@Override
	public ProductDto updateProductWithCategory(String productId, String categoryId) {
		Product product = productRepository.findById(productId).
				orElseThrow(() -> new ResourceNotFoundException("Product Id not available"));
		Category category = categoryRepository.findById(categoryId).
				orElseThrow(() -> new ResourceNotFoundException("Category Id not available"));
		
		product.setCategory(category);
		Product savedProduct = productRepository.save(product);
		
		return modelMapper.map(savedProduct, ProductDto.class);
	}

	@Override
	public PageableResponse<ProductDto> getLiveProductsByCategory(String categoryId, Integer pageNumber,
			Integer pageSize, String sortBy, String sortDirection) {
		Category category = categoryRepository.findById(categoryId).
				orElseThrow(() -> new ResourceNotFoundException("Category Id not available"));
		Sort sort = Helper.getSorting(sortBy, sortDirection);
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
		Page<Product> pageProduct = productRepository.findByCategoryAndLiveTrue(category, pageable);
		PageableResponse<ProductDto> response = Helper.getPageableResponse(pageProduct, ProductDto.class);
		return response;
	}

	@Override
	public PageableResponse<ProductDto> getLiveProductsBySubTitle(String subTitle, Integer pageNumber, Integer pageSize,
			String sortBy, String sortDirection) {
		Sort sort = Helper.getSorting(sortBy, sortDirection);
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
		Page<Product> pageProduct = productRepository.findByTitleContainingAndLiveTrue(subTitle,pageable);
		PageableResponse<ProductDto> response = Helper.getPageableResponse(pageProduct, ProductDto.class);
		return response;
	}

}
