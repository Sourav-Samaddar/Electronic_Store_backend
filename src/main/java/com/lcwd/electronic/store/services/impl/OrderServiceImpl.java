package com.lcwd.electronic.store.services.impl;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.lcwd.electronic.store.dtos.CreateOrderRequest;
import com.lcwd.electronic.store.dtos.OrderDto;
import com.lcwd.electronic.store.dtos.OrderUpdateRequest;
import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.entities.Address;
import com.lcwd.electronic.store.entities.Cart;
import com.lcwd.electronic.store.entities.CartItem;
import com.lcwd.electronic.store.entities.Order;
import com.lcwd.electronic.store.entities.OrderItem;
import com.lcwd.electronic.store.entities.User;
import com.lcwd.electronic.store.exceptions.BadApiRequest;
import com.lcwd.electronic.store.exceptions.ResourceNotFoundException;
import com.lcwd.electronic.store.helper.Helper;
import com.lcwd.electronic.store.repositories.AddressRepository;
import com.lcwd.electronic.store.repositories.CartRepository;
import com.lcwd.electronic.store.repositories.OrderRepository;
import com.lcwd.electronic.store.repositories.UserRepository;
import com.lcwd.electronic.store.services.OrderService;

@Service
public class OrderServiceImpl implements OrderService{
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private AddressRepository addressRepository;
	
	@Autowired
	private ModelMapper modelMapper;

	@Override
	public OrderDto createOrder(CreateOrderRequest createOrderRequest) {
		String userId = createOrderRequest.getUserId();
		String cartId = createOrderRequest.getCartId();
		String addressId = createOrderRequest.getAddressId();
		
		User user = userRepository.findById(userId).
				orElseThrow(() -> new ResourceNotFoundException("User Id not found for the order"));
		
		Cart cart = cartRepository.findById(cartId).
				orElseThrow(() -> new ResourceNotFoundException("Card Id not found for the order"));
		
		Address address = addressRepository.findById(addressId).
				orElseThrow(() -> new ResourceNotFoundException("Address Id not found for the order"));
		
		if(cart == null) {
			throw new ResourceNotFoundException("No cart available for the user !!");
		}
		
		Order order = Order.builder()
				.orderId(UUID.randomUUID().toString())
				.orderdDate(new Date())
				.orderStatus(createOrderRequest.getOrderStatus())
				.paymentStatus(createOrderRequest.getPaymentStatus())
				.billingAddress(createOrderRequest.getBillingAddress())
				.billingName(createOrderRequest.getBillingName())
				.billingPhone(createOrderRequest.getBillingPhone())
				.deliveredDate(null)
				.user(user)
				.address(address)
				.build();
		
		List<CartItem> cartItems = cart.getCartItems();
		if(cartItems.size() <= 0) {
			throw new BadApiRequest("Invalid number of items in the cart");
		}
		
		AtomicReference<Double> orderedAmount = new AtomicReference<>(0D);
		Set<OrderItem> orderItems = cartItems.stream().map(cartItem -> {
			
			OrderItem orderItem = OrderItem.builder()
			.quantity(cartItem.getQuantity())
			.product(cartItem.getProduct())
			.totalPrice(cartItem.getQuantity() * cartItem.getProduct().getDiscountedPrice())
			.order(order)
			.build();
			
			orderedAmount.set(orderedAmount.get() + orderItem.getTotalPrice());
			
			return orderItem;
		}).collect(Collectors.toCollection(LinkedHashSet::new));
		
		order.setOrderItems(orderItems);
		order.setBillingAmount(orderedAmount.get());
		
		Order savedOrder = orderRepository.save(order);
		//Removing details from cart
		cart.getCartItems().clear();
        cartRepository.save(cart);
		
		return modelMapper.map(savedOrder, OrderDto.class);
	}

	@Override
	public void removeOrder(String orderId) {
		Order order = orderRepository.findById(orderId).
				orElseThrow(() -> new ResourceNotFoundException("Order Id not found for the order"));
		orderRepository.delete(order);
	}

	@Override
	public PageableResponse<OrderDto> getOrdersOfUser(String userId, Integer pageNumber, Integer pageSize, String sortBy,
			String sortDirection) {
		User user = userRepository.findById(userId).
				orElseThrow(() -> new ResourceNotFoundException("User Id not found for the order"));
		Sort sort = Helper.getSorting(sortBy, sortDirection);
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
		Page<Order> orderPage = orderRepository.findByUser(user, pageable);
		return Helper.getPageableResponse(orderPage, OrderDto.class);
	}

	@Override
	public PageableResponse<OrderDto> getAllOrders(Integer pageNumber, Integer pageSize, String sortBy,
			String sortDirection) {
		Sort sort = Helper.getSorting(sortBy, sortDirection);
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
		Page<Order> orderPage = orderRepository.findAll(pageable);
		return Helper.getPageableResponse(orderPage, OrderDto.class);
	}

	@Override
	public OrderDto updateOrder(String orderId, OrderUpdateRequest request) {
		Order order = orderRepository.findById(orderId).
				orElseThrow(() -> new ResourceNotFoundException("Order Id not found for the order"));
        order.setBillingName(request.getBillingName());
        order.setBillingPhone(request.getBillingPhone());
        order.setBillingAddress(request.getBillingAddress());
        order.setPaymentStatus(request.getPaymentStatus());
        order.setOrderStatus(request.getOrderStatus());
        order.setDeliveredDate(request.getDeliveredDate());
        Order updatedOrder = orderRepository.save(order);
        return modelMapper.map(updatedOrder, OrderDto.class);
	}

	@Override
	public OrderDto getOrderByOrderId(String orderId) {
		Order order = orderRepository.findById(orderId).
				orElseThrow(() -> new ResourceNotFoundException("Order Id not found for the order"));
		return modelMapper.map(order,OrderDto.class);
	}

}
