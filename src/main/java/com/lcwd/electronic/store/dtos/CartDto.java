package com.lcwd.electronic.store.dtos;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
public class CartDto {
	private String cartId;
	private Date createdAt;
	private UserDto user;
	private List<CartItemDto> cartItems = new ArrayList<>();
}
