package com.lcwd.electronic.store.controllers;

import java.io.IOException;
import java.security.Principal;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.lcwd.electronic.store.dtos.JwtRequest;
import com.lcwd.electronic.store.dtos.JwtResponse;
import com.lcwd.electronic.store.dtos.UserDto;
import com.lcwd.electronic.store.entities.User;
import com.lcwd.electronic.store.exceptions.BadApiRequest;
import com.lcwd.electronic.store.security.JwtTokenHelper;
import com.lcwd.electronic.store.services.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
//@CrossOrigin("*")
public class AuthController {
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private JwtTokenHelper jwtTokenHelper;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private UserService userService;
	
	@Value("${googleClientId}")
    private String googleClientId;
	
    @Value("${newPassword}")
    private String newPassword;
    
    private Logger logger = LoggerFactory.getLogger(AuthController.class);
	
	@PostMapping("/login")
	public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest jwtRequest){
		
		authenticate(jwtRequest.getEmail(),jwtRequest.getPassword());
		
		UserDetails userDetails = userDetailsService.loadUserByUsername(jwtRequest.getEmail());
		String token = jwtTokenHelper.generateToken(userDetails);
		
		JwtResponse jwtResponse = new JwtResponse();
		jwtResponse.setJwtToken(token);
		jwtResponse.setUser(this.modelMapper.map((User)userDetails, UserDto.class));
		return ResponseEntity.ok(jwtResponse);
	}
	
	private void authenticate(String userName, String password){
		try {
			authenticationManager.
			authenticate(new UsernamePasswordAuthenticationToken(userName, password));
		}catch (DisabledException e) {
			throw new BadApiRequest("User is disabled !!");
		}catch (BadCredentialsException e) {
			throw new BadApiRequest("Invalid Username or Password !!");
		}
	}

	@GetMapping("/current")
	public ResponseEntity<UserDto> getCurrentUser(Principal principal){
		return new ResponseEntity<UserDto>(modelMapper.
				map(userDetailsService.loadUserByUsername(principal.getName()), UserDto.class),HttpStatus.OK);
	}
	
	@PostMapping("/register")
	public ResponseEntity<UserDto> registerUser(@Valid @RequestBody UserDto userDto) {
		return new ResponseEntity<UserDto>(userService.createUser(userDto),HttpStatus.CREATED);
	}
	
	//login with google api

    @PostMapping("/google")
    public ResponseEntity<JwtResponse> loginWithGoogle(@RequestBody Map<String, Object> data) throws IOException {


        //get the id token from request
//        String idToken = data.get("access_token").toString();
//        
//        NetHttpTransport netHttpTransport = new NetHttpTransport();
//
//        JacksonFactory jacksonFactory = JacksonFactory.getDefaultInstance();
//
//        GoogleIdTokenVerifier.Builder verifier = new GoogleIdTokenVerifier.
//        		Builder(netHttpTransport, jacksonFactory).setAudience(Collections.singleton(googleClientId));
//
//
//        GoogleIdToken googleIdToken = GoogleIdToken.parse(verifier.getJsonFactory(), idToken);
//
//
//        GoogleIdToken.Payload payload = googleIdToken.getPayload();
//
//        logger.info("Payload : {}", payload);
//
//        String email = payload.getEmail();
    	
    	
    	String email =  data.get("email").toString();
        User user = null;

        user = userService.findUserByEmailOptional(email).orElse(null);

        if (user == null) {
            //create new user
            user = this.saveUser(email, data.get("name").toString(), data.get("picture").toString());
        }
        ResponseEntity<JwtResponse> jwtResponseResponseEntity = this.login(JwtRequest.builder().
        		email(user.getEmail()).password(newPassword).build());
        return jwtResponseResponseEntity;


    }

    private User saveUser(String email, String name, String photoUrl) {

        UserDto newUser = UserDto.builder()
                .name(name)
                .email(email)
                .password(newPassword)
                .imageName(photoUrl)
                .roles(new HashSet<>())
                .build();

        UserDto user = userService.createUser(newUser);

        return this.modelMapper.map(user, User.class);


    }
}
