package com.lcwd.electronic.store.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressDto {
	private String id;

	private String firstName;
	
	private String lastName;
	
    private String streetAddress;

    private String city;

    private String state;

    private String zipCode;
    
    private String mobile;
    
    private UserDto user;
    
}
