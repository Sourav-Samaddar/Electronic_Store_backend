package com.lcwd.electronic.store.services.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
import com.lcwd.electronic.store.entities.Category;
import com.lcwd.electronic.store.exceptions.ResourceNotFoundException;
import com.lcwd.electronic.store.helper.Helper;
import com.lcwd.electronic.store.repositories.CategoryRepository;
import com.lcwd.electronic.store.services.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService{

	@Autowired
	private CategoryRepository categoryRepo;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Value("${categories.profile.image.path}")
	private String imagePath;
	
	@Override
	public CategoryDto createCategory(CategoryDto categoryDto) {
		String categoryId = UUID.randomUUID().toString();
		categoryDto.setCategoryId(categoryId);
		Category category = categoryRepo.save(modelMapper.map(categoryDto, Category.class));
		return modelMapper.map(category, CategoryDto.class);
	}

	@Override
	public CategoryDto updateCategory(CategoryDto categoryDto, String categoryId) {
		Category category = categoryRepo.findById(categoryId).
				orElseThrow(() -> new ResourceNotFoundException("Category not found with id !!"));
		category.setTitle(categoryDto.getTitle());
		category.setDescription(categoryDto.getDescription());
		category.setCoverImage(categoryDto.getCoverImage());
		Category savedCategory = categoryRepo.save(category);
		return modelMapper.map(savedCategory, CategoryDto.class);
	}

	@Override
	public void deleteCategory(String categoryId) {
		Category category = categoryRepo.findById(categoryId).
				orElseThrow(() -> new ResourceNotFoundException("Category not found with id !!"));
		
		String fullPath = imagePath + category.getCoverImage();
		Path path = Paths.get(fullPath);
		try {
			Files.deleteIfExists(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
		categoryRepo.delete(category);
	}

	@Override
	public PageableResponse<CategoryDto> getAllCategories(Integer pageNumber, Integer pageSize, String sortBy,
			String sortDirection) {
		Sort sort = Helper.getSorting(sortBy, sortDirection);
		Pageable p = PageRequest.of(pageNumber, pageSize, sort);
		Page<Category> categoryPage = categoryRepo.findAll(p);
		PageableResponse<CategoryDto> response = Helper.getPageableResponse(categoryPage, CategoryDto.class);
		return response;
	}

	@Override
	public CategoryDto getCategoryById(String categoryId) {
		Category category = categoryRepo.findById(categoryId).
				orElseThrow(() -> new ResourceNotFoundException("Category not found with id !!"));	
		return modelMapper.map(category, CategoryDto.class);
	}

}
