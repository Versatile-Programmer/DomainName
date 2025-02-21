package com.rishi.exceptions;

import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import jakarta.persistence.EntityNotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(exception = EntityNotFoundException.class)
	public ResponseEntity<?> entityExceptions(EntityNotFoundException ex,WebRequest request){
		return new ResponseEntity<>(ex.getMessage(),HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(exception =  DataRetrievalFailureException.class)
	public ResponseEntity<?> dataRetrievalExceptions( DataRetrievalFailureException ex,WebRequest request){
		return new ResponseEntity<>(ex.getMessage(),HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(exception = IllegalArgumentException.class)
	public ResponseEntity<String> illegalArgumentsException(IllegalAccessException ex,WebRequest request){
		return new ResponseEntity<>(ex.getMessage(),HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(exception =UsernameNotFoundException.class)
	public ResponseEntity<String> authException(UsernameNotFoundException ex,WebRequest request){
		return new ResponseEntity<String>(ex.getMessage(),HttpStatus.NOT_FOUND);
	}
}
