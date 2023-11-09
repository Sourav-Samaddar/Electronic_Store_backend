package com.lcwd.electronic.store.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CreateOrderRequest {
	@NotBlank(message = "userId is required !!")
	private String userId;
	
	@NotBlank(message = "cartId is required !!")
	private String cartId;
	
	private String orderStatus="ORDER PLACED";
	private String paymentStatus="NOTPAID";
	
	@NotBlank(message = "billingAddress is required !!")
	private String billingAddress;
	
	@NotBlank(message = "billingPhone is required !!")
	private String billingPhone;
	
	@NotBlank(message = "billingName is required !!")
	private String billingName;
	
	@NotBlank(message = "Address Id is required !!")
	private String addressId;
}
