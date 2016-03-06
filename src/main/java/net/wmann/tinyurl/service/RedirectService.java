package net.wmann.tinyurl.service;

import net.wmann.tinyurl.exception.IdGenerationException;
import net.wmann.tinyurl.exception.UrlNotFoundException;
import net.wmann.tinyurl.model.Response;

public interface RedirectService {
	
	String findRedirectUrl(String id) throws UrlNotFoundException;
	
	Response saveOverwrite(String id, String url);
	
	Response save(String id, String url) throws IdGenerationException;

}
