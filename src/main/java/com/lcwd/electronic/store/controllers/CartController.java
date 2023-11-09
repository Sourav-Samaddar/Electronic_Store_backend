package com.lcwd.electronic.store.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lcwd.electronic.store.dtos.AddItemToCartRequest;
import com.lcwd.electronic.store.dtos.ApiResponseMessage;
import com.lcwd.electronic.store.dtos.CartDto;
import com.lcwd.electronic.store.services.CartService;

@RestController
@RequestMapping("/cart")
public class CartController {
	
	@Autowired
	CartService cartService;

	@PostMapping("/{userId}")
	public ResponseEntity<CartDto> addItemToCart(@PathVariable(name = "userId") String userId,
			@RequestBody AddItemToCartRequest request) {
		return new ResponseEntity<CartDto>(cartService.addItemToCart(userId, request),HttpStatus.CREATED);
	}
	
	@DeleteMapping("/{userId}/item/{cartItem}")
	public ResponseEntity<ApiResponseMessage> removeItemFromCart(@PathVariable(name = "userId") String userId, 
			@PathVariable(name = "cartItem") int cartItem) {
		cartService.removeItemFromCart(userId, cartItem);
		return new ResponseEntity<ApiResponseMessage>(ApiResponseMessage
				.builder()
				.message("Cart Item removed successfully !!")
				.success(true)
				.status(HttpStatus.OK)
				.build(),HttpStatus.OK);
	}
	
	@DeleteMapping("/{userId}")
	public ResponseEntity<ApiResponseMessage> clearCart(@PathVariable(name = "userId") String userId) {
		cartService.clearCart(userId);
		return new ResponseEntity<ApiResponseMessage>(ApiResponseMessage
				.builder()
				.message("Cart cleared successfully !!")
				.success(true)
				.status(HttpStatus.OK)
				.build(),HttpStatus.OK);
	}
	
	@GetMapping("/{userId}")
	public ResponseEntity<CartDto> getCartByUser(@PathVariable(name = "userId") String userId) {
		return new ResponseEntity<CartDto>(cartService.getCartByUser(userId),HttpStatus.OK);
	}
}
