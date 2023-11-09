package com.lcwd.electronic.store.exceptions;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.lcwd.electronic.store.dtos.ApiResponseMessage;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ApiResponseMessage> resourceNotFoundExceptionHandler(ResourceNotFoundException rex){
		return new ResponseEntity<ApiResponseMessage>(ApiResponseMessage.
				builder()
				.message(rex.getMessage())
				.success(false)
				.status(HttpStatus.NOT_FOUND)
				.build(),HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(BadApiRequest.class)
	public ResponseEntity<ApiResponseMessage> badApiRequestHandler(BadApiRequest rex){
		return new ResponseEntity<ApiResponseMessage>(ApiResponseMessage.
				builder()
				.message(rex.getMessage())
				.success(false)
				.status(HttpStatus.BAD_REQUEST)
				.build(),HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String,String>> methodArgumentNotValidException(MethodArgumentNotValidException mex){
		Map<String,String> res = new HashMap<>();
		mex.getBindingResult().getAllErrors().forEach(error->{
			String fieldName = ((FieldError)error).getField();
			String message = error.getDefaultMessage();
			res.put(fieldName, message);
		});
		return new ResponseEntity<Map<String,String>>(res,HttpStatus.BAD_REQUEST);
	}
}
