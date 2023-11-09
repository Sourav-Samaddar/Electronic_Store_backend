package com.lcwd.electronic.store.services;

import com.lcwd.electronic.store.dtos.CreateOrderRequest;
import com.lcwd.electronic.store.dtos.OrderDto;
import com.lcwd.electronic.store.dtos.OrderUpdateRequest;
import com.lcwd.electronic.store.dtos.PageableResponse;

public interface OrderService {
	OrderDto createOrder(CreateOrderRequest createOrderRequest);
	void removeOrder(String orderId);
	PageableResponse<OrderDto> getOrdersOfUser(String userId, Integer pageNumber, Integer pageSize, String sortBy,
			String sortDirection);
	PageableResponse<OrderDto> getAllOrders(Integer pageNumber, Integer pageSize, String sortBy,
			String sortDirection);
	OrderDto updateOrder(String orderId, OrderUpdateRequest request);
	public OrderDto getOrderByOrderId(String orderId);
}
