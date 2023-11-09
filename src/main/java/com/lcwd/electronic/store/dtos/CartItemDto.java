package com.lcwd.electronic.store.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CartItemDto {
	private int cartItemId;
	private ProductDto product;
	private int quantity;
	private double totalPrice;
}
