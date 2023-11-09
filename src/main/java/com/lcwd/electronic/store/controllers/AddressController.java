package com.lcwd.electronic.store.controllers;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lcwd.electronic.store.dtos.AddressDto;
import com.lcwd.electronic.store.services.AddressService;

@RestController
@RequestMapping("/address")
public class AddressController {
	
	@Autowired
	private AddressService addressService;

	@GetMapping("/user/{userId}")
	public ResponseEntity<Set<AddressDto>> getAddressOfUser(@PathVariable String userId){
		return new ResponseEntity<Set<AddressDto>>(addressService.getAddressByUser(userId),HttpStatus.OK);
	}
	
	@PostMapping("/user/{userId}")
	public ResponseEntity<AddressDto> addAddressForUser(@PathVariable String userId, 
			@RequestBody AddressDto addressDto){
		return new ResponseEntity<AddressDto>(addressService.addAddressForUser(addressDto, userId),HttpStatus.CREATED);
	}
}
