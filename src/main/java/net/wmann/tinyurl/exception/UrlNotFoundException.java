package net.wmann.tinyurl.exception;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UrlNotFoundException extends Exception {
	
	private static final long serialVersionUID = -1727931571028212808L;
	
	private String id;

}
