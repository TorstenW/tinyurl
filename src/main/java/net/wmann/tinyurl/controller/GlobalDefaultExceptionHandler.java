package net.wmann.tinyurl.controller;

import javax.servlet.http.HttpServletRequest;

import net.wmann.tinyurl.exception.IdGenerationException;
import net.wmann.tinyurl.exception.UrlNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalDefaultExceptionHandler {
	
	@ExceptionHandler(value = IdGenerationException.class)
    public ResponseEntity<String> idGenerationError(HttpServletRequest req, Exception e) {
		return new ResponseEntity<>("Internal server error while saving url", HttpStatus.INTERNAL_SERVER_ERROR);
    }
	
	@ExceptionHandler(value = UrlNotFoundException.class)
    public ResponseEntity<String> urlNotFoundError(HttpServletRequest req, UrlNotFoundException e) {
		return new ResponseEntity<>("No URL was found for id " + e.getId(), HttpStatus.BAD_REQUEST);
    }

}
