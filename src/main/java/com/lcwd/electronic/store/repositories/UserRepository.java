package com.lcwd.electronic.store.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lcwd.electronic.store.entities.User;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, String>{
	Optional<User> findByEmail(String email);
	Optional<User> findByEmailAndPassword(String email,String password);
	Page<User> findByNameContaining(String keyword,Pageable p);
}
