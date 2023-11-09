package com.lcwd.electronic.store.dtos;

import java.util.Date;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
public class ProductDto {

	private String productId;
	
	@NotBlank(message = "Product title is required !!")
	@Size(min = 4,message = "Product title should be minimum 4 characters !!")
	private String title;
	
	@NotBlank(message = "Product short name is required !!")
	@Size(min = 4,max=25,message = "Product title should be minimum 4 and maximum 20 characters !!")
	private String shortName;
	
	@NotBlank(message = "Product description is required !!")
	private String description;
	
	//@NotBlank(message = "Product price is required !!")
	private double price;
	
	private double discountedPrice;
	
	private int quantity;
	
	private Date addedDate;
	
	private boolean live;
	
	private boolean stock;
	
	private String productImageName;
	
	private CategoryDto category;
}
