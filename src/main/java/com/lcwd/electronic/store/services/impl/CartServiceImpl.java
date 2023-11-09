package com.lcwd.electronic.store.services.impl;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lcwd.electronic.store.dtos.AddItemToCartRequest;
import com.lcwd.electronic.store.dtos.CartDto;
import com.lcwd.electronic.store.entities.Cart;
import com.lcwd.electronic.store.entities.CartItem;
import com.lcwd.electronic.store.entities.Product;
import com.lcwd.electronic.store.entities.User;
import com.lcwd.electronic.store.exceptions.BadApiRequest;
import com.lcwd.electronic.store.exceptions.ResourceNotFoundException;
import com.lcwd.electronic.store.repositories.CartItemRepository;
import com.lcwd.electronic.store.repositories.CartRepository;
import com.lcwd.electronic.store.repositories.ProductRepository;
import com.lcwd.electronic.store.repositories.UserRepository;
import com.lcwd.electronic.store.services.CartService;

@Service
public class CartServiceImpl implements CartService{
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CartRepository cartRepository;	
	
	@Autowired
	private CartItemRepository cartItemRepository;
	
	@Autowired
	private ModelMapper mapper;

	@Override
	public CartDto addItemToCart(String userId, AddItemToCartRequest request) {
		String productId = request.getProductId();
		int quantity = request.getQuantity();
		
		if(quantity <= 0) {
			throw new BadApiRequest("Requested quantity not available !!");
		}
		
		Product product = productRepository.findById(productId).
				orElseThrow(() -> new ResourceNotFoundException("Product Id not available !!"));
		
		User user = userRepository.findById(userId).
				orElseThrow(() -> new ResourceNotFoundException("User Id not available !!"));
		
		Cart cart = null;
        try {
            cart = cartRepository.findByUser(user).get();
        } catch (NoSuchElementException e) {
            cart = new Cart();
            cart.setCartId(UUID.randomUUID().toString());
            cart.setCreatedAt(new Date());
        }
		
		AtomicReference<Boolean> updated = new AtomicReference<>(false);
		
		List<CartItem> items = cart.getCartItems();
		
		items = items.stream().map(item -> {
			if(item.getProduct().getProductId().equals(productId)) {
				item.setQuantity(quantity);
				item.setTotalPrice(quantity*product.getDiscountedPrice());
				updated.set(true);
			}
			
			return item;
		}).collect(Collectors.toList());
		
		if(!updated.get()) {
			//Create cart item
			CartItem cartItem = CartItem.builder()
			.quantity(quantity)
			.totalPrice(quantity*product.getDiscountedPrice())
			.product(product)
			.cart(cart)
			.build();
			
			cart.getCartItems().add(cartItem);
		}
		
		cart.setUser(user);
		Cart updatedCart = cartRepository.save(cart);
		
		return mapper.map(updatedCart, CartDto.class);
	}

	@Override
	public void removeItemFromCart(String userId, int cartItem) {
		CartItem carItem = cartItemRepository.findById(cartItem).
				orElseThrow(() -> new ResourceNotFoundException("Cart Item not found !!"));
		cartItemRepository.delete(carItem);
	}

	@Override
	public void clearCart(String userId) {
		User user = userRepository.findById(userId).
				orElseThrow(() -> new ResourceNotFoundException("User not found !!"));
		Cart cart = cartRepository.findByUser(user).
				orElseThrow(() -> new ResourceNotFoundException("Cart of given user not found !!"));
		cart.getCartItems().clear();
        cartRepository.save(cart);
	}

	@Override
	public CartDto getCartByUser(String userId) {
		User user = userRepository.findById(userId).
				orElseThrow(() -> new ResourceNotFoundException("Cart of given user not found !!"));
		Cart cart = cartRepository.findByUser(user).
				orElseThrow(() -> new ResourceNotFoundException("Cart of given user not found !!"));
		return mapper.map(cart, CartDto.class);
	}

}
