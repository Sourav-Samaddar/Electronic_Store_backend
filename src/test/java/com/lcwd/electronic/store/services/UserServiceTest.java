package com.lcwd.electronic.store.services;

import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.lcwd.electronic.store.dtos.UserDto;
import com.lcwd.electronic.store.entities.Role;
import com.lcwd.electronic.store.entities.User;
import com.lcwd.electronic.store.helper.Helper;
import com.lcwd.electronic.store.repositories.RoleRepository;
import com.lcwd.electronic.store.repositories.UserRepository;
import java.util.Arrays;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.lcwd.electronic.store.dtos.PageableResponse;

@SpringBootTest
public class UserServiceTest {

	@MockBean
	private UserRepository userRepo;
	
	@MockBean
	private RoleRepository roleRepo;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ModelMapper mapper;
	
	private User user;
	
	private Role role;
	
	@BeforeEach
	public void init() {
		role = Role.builder().roleId("abc").roleName("Normal").build();
		user = User.builder()
		.name("Sourav")
		.email("sourav.smddr@gmail.com")
		.password("penguine")
		.about("I am a software developer")
		.gender("Male")
		.roles(Set.of(role))
		.build();
	}
	
	@Test
	public void createTestUser() {
		
		Mockito.when(roleRepo.findById(Mockito.anyString())).thenReturn(Optional.of(role));
		Mockito.when(userRepo.save(Mockito.any())).thenReturn(user);
		UserDto userDto = userService.createUser(mapper.map(user, UserDto.class));
		Assertions.assertEquals("Sourav", userDto.getName());
	}
	
	@Test
	public void updateTestUser() {
		String userId = "";
		UserDto userDto = UserDto.builder()
				.name("Sourav")
				.email("sourav.smddr@gmail.com")
				.password("penguine")
				.about("I am a software developer")
				.gender("Male")
				.build();
		
		Mockito.when(userRepo.findById(Mockito.anyString())).thenReturn(Optional.of(user));
		Mockito.when(userRepo.save(Mockito.any())).thenReturn(user);
		
		UserDto updatedUser = userService.updateUser(userDto, userId);
		Assertions.assertEquals("sourav.smddr@gmail.com", updatedUser.getEmail());
	}
	
	@Test
    public void deleteUserTest() {
        String userid = "userIdabc";
        Mockito.when(userRepo.findById("userIdabc")).thenReturn(Optional.of(user));
        System.out.println(user.getRoles());
        user.getRoles().clear();
        userService.deleteUser(userid);
        Mockito.verify(userRepo, Mockito.times(1)).delete(user);
    }
	
	@Test
    public void getAllUsersTest() {
        User user1 = User.builder()
                .name("Ankit")
                .email("durgesh@gmail.com")
                .about("This is testing create method")
                .gender("Male")
                .imageName("abc.png")
                .password("lcwd")
                .roles(Set.of(role))
                .build();
        User user2 = User.builder()
                .name("Uttam")
                .email("durgesh@gmail.com")
                .about("This is testing create method")
                .gender("Male")
                .imageName("abc.png")
                .password("lcwd")
                .roles(Set.of(role))
                .build();
        List<User> userList = Arrays.asList(user, user1, user2);
        Page<User> page = new PageImpl<>(userList);
        Mockito.when(userRepo.findAll((Pageable) Mockito.any())).thenReturn(page);
        PageableResponse<UserDto> allUser = userService.getAllUsers(1, 2, "name", "asc");
        Assertions.assertEquals(3, allUser.getContent().size());

    }
	
	@Test
    public void getUserByIdTest() {

        String userId = "userIdTest";
        Mockito.when(userRepo.findById(userId)).thenReturn(Optional.of(user));

        //actual call of service method

        UserDto userDto = userService.getUser(userId);

        Assertions.assertNotNull(userDto);
        Assertions.assertEquals(user.getName(), userDto.getName(), "Name not matched !!");

    }
	
	@Test
    public void searchUserTest() {

        User user1 = User.builder()
                .name("Ankit Kumar")
                .email("durgesh@gmail.com")
                .about("This is testing create method")
                .gender("Male")
                .imageName("abc.png")
                .password("lcwd")
                .roles(Set.of(role))
                .build();

        User user2 = User.builder()
                .name("Uttam Kumar")
                .email("durgesh@gmail.com")
                .about("This is testing create method")
                .gender("Male")
                .imageName("abc.png")
                .password("lcwd")
                .roles(Set.of(role))
                .build();

        User user3 = User.builder()
                .name("Pankaj Kumar")
                .email("durgesh@gmail.com")
                .about("This is testing create method")
                .gender("Male")
                .imageName("abc.png")
                .password("lcwd")
                .roles(Set.of(role))
                .build();

        String keywords = "Kumar";
        List<User> userList = Arrays.asList(user, user1, user2, user3);
        Page<User> pageUser = new PageImpl<>(userList);
        Sort sort = Helper.getSorting("name", "asc");
        Pageable p = PageRequest.of(1, 2, sort);
        Mockito.when(userRepo.findByNameContaining(keywords,p)).thenReturn(pageUser);
        PageableResponse<UserDto> response = Helper.getPageableResponse(pageUser, UserDto.class);
        PageableResponse<UserDto> userDtos = userService.searchUser(keywords,1,2,"name", "asc");
        Assertions.assertEquals(response.getContent().size(), userDtos.getContent().size(), "size not matched !!");

    }
	
	@Test
    public void findUserByEmailOptionalTest() {

        String email = "durgeshkumar@gmail.com";

        Mockito.when(userRepo.findByEmail(email)).thenReturn(Optional.of(user));
        Optional<User> userByEmailOptional = userService.findUserByEmailOptional(email);
        Assertions.assertTrue(userByEmailOptional.isPresent());
        User user1 = userByEmailOptional.get();
        Assertions.assertEquals(user.getEmail(), user1.getEmail(), "email does not matched ");


    }
}
