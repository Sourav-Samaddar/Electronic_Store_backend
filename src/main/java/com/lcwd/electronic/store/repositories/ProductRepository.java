package com.lcwd.electronic.store.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lcwd.electronic.store.entities.Category;
import com.lcwd.electronic.store.entities.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, String>{
	Page<Product> findByTitleContaining(String keyword, Pageable p);
	Page<Product> findByCategory(Category category, Pageable p);
	Page<Product> findByLiveTrue(Pageable p);
	Page<Product> findByStockTrue(Pageable p);
	Page<Product> findByCategoryAndLiveTrue(Category category, Pageable p);
	Page<Product> findByTitleContainingAndLiveTrue(String keyword, Pageable p);
}
