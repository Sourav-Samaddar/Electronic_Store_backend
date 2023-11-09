package com.lcwd.electronic.store.dtos;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcwd.electronic.store.validate.ImageNameValid;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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
public class UserDto {

	private String userId;
	
	@Size(min = 3, max = 50, message = "Name is too small or big !!")
	private String name;
	
	//@Email(message = "Please enter valid email id !!")
	@Pattern(regexp = "^[a-z0-9][-a-z0-9._]+@([-a-z0-9]+\\.)+[a-z]{2,5}$",
			message = "Invalid Email Id")
	@NotBlank(message = "Email is required !!")
	private String email;
	
	//@NotBlank(message = "Password is required !!")
	@Size(min = 4, message = "Password should be minimum 4 characters")
	private String password;
	
	@Size(min = 4, max = 6, message = "Invalid gender!!")
	private String gender;
	
	@NotBlank(message = "Please write something about yourself !!")
	private String about;
	
	@ImageNameValid
	private String imageName;
	
	private Set<RoleDto> roles = new HashSet<>();
	
	@JsonIgnore
	public String getPassword() {
		return this.password;
	}
	
	@JsonProperty
	public void setPassword(String password) {
		this.password = password;
	}
}
