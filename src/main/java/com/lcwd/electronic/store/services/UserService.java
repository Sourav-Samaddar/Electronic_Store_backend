package com.lcwd.electronic.store.services;

import java.util.Optional;

import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.dtos.UserDto;
import com.lcwd.electronic.store.entities.User;

public interface UserService {

	UserDto createUser(UserDto userDto);
	
	UserDto updateUser(UserDto userDto, String userId);
	
	void deleteUser(String userId);
	
	PageableResponse<UserDto> getAllUsers(Integer pageNumber, Integer pageSize, String sortBy,
			String sortDirection);
	
	UserDto getUser(String userId);
	
	UserDto getUserByEmail(String emailId);
	
	PageableResponse<UserDto> searchUser(String keyword, Integer pageNumber, Integer pageSize, String sortBy,
			String sortDirection);
	
	Optional<User> findUserByEmailOptional(String email);
}
