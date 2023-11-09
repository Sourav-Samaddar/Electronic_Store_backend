package com.lcwd.electronic.store.dtos;

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
public class CategoryDto {
	
	private String categoryId;
	
	@NotBlank(message = "category title required !!")
	@Size(min = 4, message = "title must be minimum 4 characters !!")
	private String title;
		
	@NotBlank(message = "category description required !!")
	private String description;
	
	private String coverImage;
}
