package net.wmann.tinyurl.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UrlResponse implements Response {

	private static final long serialVersionUID = -3823152849036775735L;

	private String id;
	
	private String url;
	
	private String message;
	
	private boolean error = false;
	
}
