package com.lcwd.electronic.store.services;


import com.lcwd.electronic.store.dtos.CategoryDto;
import com.lcwd.electronic.store.dtos.PageableResponse;

public interface CategoryService {
	
	CategoryDto createCategory(CategoryDto categoryDto);
	
	CategoryDto updateCategory(CategoryDto categoryDto,String categoryId);
	
	void deleteCategory(String categoryId);
	
	PageableResponse<CategoryDto> getAllCategories(Integer pageNumber, Integer pageSize, String sortBy,
			String sortDirection);
	
	CategoryDto getCategoryById(String categoryId);
}
