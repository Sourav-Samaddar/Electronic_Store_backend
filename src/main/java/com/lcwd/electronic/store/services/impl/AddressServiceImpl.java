package com.lcwd.electronic.store.services.impl;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lcwd.electronic.store.dtos.AddressDto;
import com.lcwd.electronic.store.entities.Address;
import com.lcwd.electronic.store.entities.User;
import com.lcwd.electronic.store.exceptions.ResourceNotFoundException;
import com.lcwd.electronic.store.repositories.AddressRepository;
import com.lcwd.electronic.store.repositories.UserRepository;
import com.lcwd.electronic.store.services.AddressService;

@Service
public class AddressServiceImpl implements AddressService{
	
	@Autowired
	private AddressRepository addressRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ModelMapper modelMapper;

	@Override
	public Set<AddressDto> getAddressByUser(String userId) {
		User user = userRepository.findById(userId).
				orElseThrow(() -> new ResourceNotFoundException("User Id not found for the order"));
		Set<Address> addressList = addressRepository.getAddressByUser(user);
		return addressList.stream().map(address -> modelMapper.map(address,AddressDto.class)).
				collect(Collectors.toSet());
		
	}

	@Override
	public AddressDto addAddressForUser(AddressDto addressDto, String userId) {
		User user = userRepository.findById(userId).
				orElseThrow(() -> new ResourceNotFoundException("User Id not found for the order"));
		Address address = modelMapper.map(addressDto, Address.class);
		address.setId(UUID.randomUUID().toString());
		address.setUser(user);
		Address savedAddress = addressRepository.save(address);
		return modelMapper.map(savedAddress, AddressDto.class);
	}
	

}
