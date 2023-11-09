package com.lcwd.electronic.store.services;

import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.dtos.ProductDto;

public interface ProductService {
	ProductDto createProduct(ProductDto productDto,String categoryId);
	ProductDto createProductWithoutCaterory(ProductDto productDto);
	ProductDto updateProduct(ProductDto productDto, String productId);
	ProductDto updateProductWithCategory(String productId, String categoryId);
	void deleteProduct(String productId);
	PageableResponse<ProductDto> getAllProducts(Integer pageNumber, Integer pageSize, String sortBy,
			String sortDirection);
	PageableResponse<ProductDto> getAllLiveProducts(Integer pageNumber, Integer pageSize, String sortBy,
			String sortDirection);
	PageableResponse<ProductDto> getAllProductsInStock(Integer pageNumber, Integer pageSize, String sortBy,
			String sortDirection);
	PageableResponse<ProductDto> getProductsBySubTitle(String subTitle, Integer pageNumber, 
			Integer pageSize, String sortBy, String sortDirection);
	ProductDto getProductById(String productId);
	PageableResponse<ProductDto> getProductsByCategory(String categoryId, Integer pageNumber, 
			Integer pageSize, String sortBy, String sortDirection);
	PageableResponse<ProductDto> getLiveProductsByCategory(String categoryId, Integer pageNumber, 
			Integer pageSize, String sortBy, String sortDirection);
	PageableResponse<ProductDto> getLiveProductsBySubTitle(String subTitle, Integer pageNumber, 
			Integer pageSize, String sortBy, String sortDirection);
}
