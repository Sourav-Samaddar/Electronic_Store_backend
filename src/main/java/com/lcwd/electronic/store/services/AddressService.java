package com.lcwd.electronic.store.services;

import java.util.Set;

import com.lcwd.electronic.store.dtos.AddressDto;

public interface AddressService {
	public Set<AddressDto> getAddressByUser(String userId);
	public AddressDto addAddressForUser(AddressDto addressDto, String userId);
}
