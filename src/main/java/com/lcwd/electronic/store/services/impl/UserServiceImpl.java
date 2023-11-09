package com.lcwd.electronic.store.services.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.dtos.UserDto;
import com.lcwd.electronic.store.entities.Role;
import com.lcwd.electronic.store.entities.User;
import com.lcwd.electronic.store.exceptions.ResourceNotFoundException;
import com.lcwd.electronic.store.helper.Helper;
import com.lcwd.electronic.store.repositories.RoleRepository;
import com.lcwd.electronic.store.repositories.UserRepository;
import com.lcwd.electronic.store.services.UserService;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private RoleRepository roleRepo;
	
	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Value("${user.profile.image.path}")
	private String imagePath;
	
	@Value("${normal.role.id}")
    private String normalRoleId;
	
	@Override
	public UserDto createUser(UserDto userDto) {
		String userId = UUID.randomUUID().toString();
		userDto.setUserId(userId);
		userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
		Role role = roleRepo.findById(normalRoleId).get();
		User user = dtoToEntity(userDto);
		user.getRoles().add(role);
		User savedUser = userRepo.save(user);
		return entityToDto(savedUser);
	}

	@Override
	public UserDto updateUser(UserDto userDto, String userId) {
		User user = userRepo.findById(userId).
				orElseThrow(()->new ResourceNotFoundException("User not found with id !!"));
		user.setName(userDto.getName());
		//user.setEmail(userDto.getEmail());
		if(userDto.getPassword() != null && !user.getPassword().equalsIgnoreCase(userDto.getPassword()) ) {
			user.setPassword(passwordEncoder.encode(userDto.getPassword()));
		}
		
		user.setAbout(userDto.getAbout());
		user.setGender(userDto.getGender());
		if(userDto.getImageName() != null && !userDto.getImageName().equalsIgnoreCase("default.png")) {
			user.setImageName(userDto.getImageName());
		}
		
		User savedUser = userRepo.save(user);
		return entityToDto(savedUser);
	}

	@Override
	public void deleteUser(String userId) {
		User user = userRepo.findById(userId).
				orElseThrow(()->new ResourceNotFoundException("User not found with id !!"));
		
		String fullPath = imagePath + user.getImageName();
		Path path = Paths.get(fullPath);
		try {
			Files.deleteIfExists(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
		user.getRoles().clear();
		userRepo.save(user);
		userRepo.delete(user);
	}

	@Override
	public PageableResponse<UserDto> getAllUsers(Integer pageNumber, Integer pageSize, String sortBy,
			String sortDirection) {
		Sort sort = Helper.getSorting(sortBy, sortDirection);
		Pageable p = PageRequest.of(pageNumber, pageSize, sort);
		Page<User> pageUser = userRepo.findAll(p);
		PageableResponse<UserDto> response = Helper.getPageableResponse(pageUser, UserDto.class);
		return response;
	}

	@Override
	public UserDto getUser(String userId) {
		User user = userRepo.findById(userId).
				orElseThrow(()->new ResourceNotFoundException("User not found with id !!"));
		return entityToDto(user);
	}

	@Override
	public UserDto getUserByEmail(String emailId) {
		User user = userRepo.findByEmail(emailId).
				orElseThrow(()->new ResourceNotFoundException("User not found with email !!"));
		return entityToDto(user);
	}

	@Override
	public PageableResponse<UserDto> searchUser(String keyword, Integer pageNumber, Integer pageSize, String sortBy,
			String sortDirection) {
		Sort sort = Helper.getSorting(sortBy, sortDirection);
		Pageable p = PageRequest.of(pageNumber, pageSize, sort);
		Page<User> pageUser = userRepo.findByNameContaining(keyword,p);
		PageableResponse<UserDto> response = Helper.getPageableResponse(pageUser, UserDto.class);
		return response;
	}
	
	private User dtoToEntity(UserDto userDto) {
//		return User.builder()
//				.userId(userDto.getUserId())
//				.name(userDto.getName())
//				.email(userDto.getEmail())
//				.password(userDto.getPassword())
//				.gender(userDto.getGender())
//				.about(userDto.getAbout())
//				.imageName(userDto.getImageName())
//				.build();
		return mapper.map(userDto, User.class);
	}
	
	private UserDto entityToDto(User user) {
//		return UserDto.builder()
//				.userId(user.getUserId())
//				.name(user.getName())
//				.email(user.getEmail())
//				.password(user.getPassword())
//				.gender(user.getGender())
//				.about(user.getAbout())
//				.imageName(user.getImageName())
//				.build();
		return mapper.map(user,UserDto.class);
	}

	@Override
	public Optional<User> findUserByEmailOptional(String email) {
		return userRepo.findByEmail(email);
	}

}
