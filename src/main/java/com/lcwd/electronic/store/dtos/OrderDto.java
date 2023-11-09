package com.lcwd.electronic.store.dtos;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

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
public class OrderDto {

	private String orderId;
	private String orderStatus="PENDING";
	private String paymentStatus="NOTPAID";
	private double billingAmount;
	private String billingAddress;
	private String billingPhone;
	private String billingName;
	private Date orderdDate=new Date();
	private String paymentId;
	private Date deliveredDate;
	private UserDto user;
	private Set<OrderItemDto> orderItems = new LinkedHashSet<>();
	private AddressDto address;
}
