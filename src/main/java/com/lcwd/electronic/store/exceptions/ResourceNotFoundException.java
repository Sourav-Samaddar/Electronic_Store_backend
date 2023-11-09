package com.lcwd.electronic.store.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.Builder;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
@Builder
public class ResourceNotFoundException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public ResourceNotFoundException() {
		super("Resource not found !!");
	}
	
	public ResourceNotFoundException(String errorMessage) {
		super(errorMessage);
	}
}