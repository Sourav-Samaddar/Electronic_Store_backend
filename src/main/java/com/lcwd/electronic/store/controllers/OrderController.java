package com.lcwd.electronic.store.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lcwd.electronic.store.config.AppConstant;
import com.lcwd.electronic.store.dtos.ApiResponseMessage;
import com.lcwd.electronic.store.dtos.CreateOrderRequest;
import com.lcwd.electronic.store.dtos.OrderDto;
import com.lcwd.electronic.store.dtos.OrderUpdateRequest;
import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.services.OrderService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/orders")
public class OrderController {
	
	@Autowired
	private OrderService orderService;

	@PostMapping
	public ResponseEntity<OrderDto> createOrder(@Valid @RequestBody CreateOrderRequest createOrderRequest) {
		return new ResponseEntity<OrderDto>(orderService.createOrder(createOrderRequest),HttpStatus.CREATED);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{orderId}")
	public ResponseEntity<ApiResponseMessage> removeOrder(@PathVariable(name = "orderId") String orderId) {
		
		orderService.removeOrder(orderId);
		
		return new ResponseEntity<ApiResponseMessage>(ApiResponseMessage
				.builder()
				.message("Product deleted successfully")
				.success(true)
				.status(HttpStatus.OK)
				.build(),HttpStatus.OK);
	}
	
	@GetMapping("/user/{userId}")
	public ResponseEntity<PageableResponse<OrderDto>> getOrdersOfUser(@PathVariable(name = "userId") String userId,
			@RequestParam (value = "pageNumber",
			defaultValue = com.lcwd.electronic.store.config.AppConstant.PAGE_NUMBER,required = false) Integer pageNumber,
			@RequestParam (value = "pageSize",
			defaultValue = AppConstant.PAGE_SIZE,required = false) Integer pageSize,
			@RequestParam (value = "sortBy",
			defaultValue = AppConstant.SORT_BY_ORDERED_DATE,required = false) String sortBy,
			@RequestParam (value = "sortDirection",
			defaultValue = AppConstant.SORT_DIRECTION,required = false) String sortDirection) {
		return new ResponseEntity<PageableResponse<OrderDto>>(orderService.
				getOrdersOfUser(userId,pageNumber, pageSize, sortBy, sortDirection),HttpStatus.OK);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping
	public ResponseEntity<PageableResponse<OrderDto>> getAllOrders(
			@RequestParam (value = "pageNumber",
			defaultValue = com.lcwd.electronic.store.config.AppConstant.PAGE_NUMBER,required = false) Integer pageNumber,
			@RequestParam (value = "pageSize",
			defaultValue = AppConstant.PAGE_SIZE,required = false) Integer pageSize,
			@RequestParam (value = "sortBy",
			defaultValue = AppConstant.SORT_BY_ORDERED_DATE,required = false) String sortBy,
			@RequestParam (value = "sortDirection",
			defaultValue = AppConstant.SORT_DIRECTION,required = false) String sortDirection){
		
		return new ResponseEntity<PageableResponse<OrderDto>>(orderService.
				getAllOrders(pageNumber, pageSize, sortBy, sortDirection),HttpStatus.OK);
	}
	
	@GetMapping("/{orderId}")
	public ResponseEntity<OrderDto> getOrderByOrderId (@PathVariable("orderId") String orderId) {
		return new ResponseEntity<OrderDto>(orderService.getOrderByOrderId(orderId),HttpStatus.OK);
	}
	
	@PutMapping("/{orderId}")
    public ResponseEntity<OrderDto> updateOrder(
            @PathVariable("orderId") String orderId,
            @Valid @RequestBody OrderUpdateRequest request
    ) {

        OrderDto dto = this.orderService.updateOrder(orderId,request);
        return ResponseEntity.ok(dto);


    }
	
}
