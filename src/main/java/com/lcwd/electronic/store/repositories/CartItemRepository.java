package com.lcwd.electronic.store.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lcwd.electronic.store.entities.CartItem;

@Repository 
public interface CartItemRepository extends JpaRepository<CartItem, Integer>{

}
