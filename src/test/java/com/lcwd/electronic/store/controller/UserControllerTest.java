package com.lcwd.electronic.store.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.dtos.UserDto;
import com.lcwd.electronic.store.entities.Role;
import com.lcwd.electronic.store.entities.User;
import com.lcwd.electronic.store.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.Set;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @MockBean
    private UserService userService;

    private Role role;
    private User user;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void init() {

        role = Role.builder().roleId("abc").roleName("NORMAL").build();

        user = User.builder()
                .name("Durgesh")
                .email("durgesh@gmail.com")
                .about("This is testing create method")
                .gender("Male")
                .imageName("abc.png")
                .password("lcwd")
                .roles(Set.of(role))
                .build();
    }

    @Test
    public void createUserTest() throws Exception {

//        /users +POST+ user data as json
        //data as json+status created

        UserDto dto = mapper.map(user, UserDto.class);

        Mockito.when(userService.createUser(Mockito.any())).thenReturn(dto);

        //actual request for url

        this.mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertObjectToJsonString(user))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").exists());
    }


    @Test
    public void updateUserTest() throws Exception {

        // /users/{userId} + PUT request+ json

        String userId = "123";
        UserDto dto = this.mapper.map(user, UserDto.class);

        Mockito.when(userService.updateUser(Mockito.any(), Mockito.anyString())).thenReturn(dto);

        this.mockMvc.perform(
                        MockMvcRequestBuilders.put("/users/" + userId)
                                .header(HttpHeaders.AUTHORIZATION, "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzb3VyYXYuc21kZHJAZ21haWwuY29tIiwiaWF0IjoxNjk1OTc1OTMxLCJleHAiOjE2OTU5OTM5MzF9.H3bkXuZZ6DZt4saTI0VaBQYsLAB-lSL9r_qbyWGHetuGsS6tcjj1qNT8KX3znzfi1gnfPUFaLQvtWDTj-MAF0w")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(convertObjectToJsonString(user))
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").exists());

    }

    private String convertObjectToJsonString(Object user) {

        try {
            return new ObjectMapper().writeValueAsString(user);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }


    }

    //get all users : testing
    @Test
    public void getAllUsersTest() throws Exception {

        UserDto object1 = UserDto.builder().name("durgesh").email("duregsh@gmail.com").password("durgesh").about("Testing").build();
        UserDto object2 = UserDto.builder().name("Amit").email("duregsh@gmail.com").password("durgesh").about("Testing").build();
        UserDto object3 = UserDto.builder().name("Sumit").email("duregsh@gmail.com").password("durgesh").about("Testing").build();
        UserDto object4 = UserDto.builder().name("Ankit").email("duregsh@gmail.com").password("durgesh").about("Testing").build();
        PageableResponse<UserDto> pageableResponse = new PageableResponse<>();
        pageableResponse.setContent(Arrays.asList(
                object1, object2, object3, object4
        ));
        pageableResponse.setIsLastPage(false);
        pageableResponse.setPageSize(10);
        pageableResponse.setPageNumber(100);
        pageableResponse.setTotalElements(1000L);
        Mockito.when(userService.getAllUsers(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString(), Mockito.anyString())).thenReturn(pageableResponse);


        this.mockMvc.perform(MockMvcRequestBuilders.get("/users")
        				.header(HttpHeaders.AUTHORIZATION, "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzb3VyYXYuc21kZHJAZ21haWwuY29tIiwiaWF0IjoxNjk1OTc1OTMxLCJleHAiOjE2OTU5OTM5MzF9.H3bkXuZZ6DZt4saTI0VaBQYsLAB-lSL9r_qbyWGHetuGsS6tcjj1qNT8KX3znzfi1gnfPUFaLQvtWDTj-MAF0w")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)

                )
                .andDo(print())
                .andExpect(status().isOk());

    }

}

