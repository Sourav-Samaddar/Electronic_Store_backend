package com.lcwd.electronic.store.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lcwd.electronic.store.entities.Order;
import com.lcwd.electronic.store.entities.User;

@Repository
public interface OrderRepository extends JpaRepository<Order, String>{
	Page<Order> findByUser(User user, Pageable p);
}
