package net.wmann.tinyurl.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.wmann.tinyurl.dao.RedirectDao;
import net.wmann.tinyurl.entity.RedirectEntity;
import net.wmann.tinyurl.exception.IdGenerationException;
import net.wmann.tinyurl.exception.UrlNotFoundException;
import net.wmann.tinyurl.model.Response;
import net.wmann.tinyurl.model.UrlResponse;
import net.wmann.tinyurl.service.RedirectService;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class RedirectServiceImpl implements RedirectService {
	
	private final static int REDIRECT_ID_LENGTH = 10;
	private final static int MAX_ID_GENERATION_TRIES = 100;
	
	private final RedirectDao redirectDao;
	
	@Autowired
	public RedirectServiceImpl(RedirectDao redirectDao) {
		this.redirectDao = redirectDao;
	}
	
	@Override
	public String findRedirectUrl(String id) throws UrlNotFoundException {
		if(id == null) {
			return null;
		}
		log.info("Getting redirect url for id {}", id);
		RedirectEntity redirectEntity = redirectDao.findOne(id);
		if(redirectEntity == null) {
			log.info("Could not find stored url for id {}", id);
			throw UrlNotFoundException.builder().id(id).build();
		}
		log.info("Found: {}", redirectEntity);
		return redirectEntity.getRedirectUrl();
	}
	
	@Override
	@Transactional
	public Response saveOverwrite(String id, String url) {
		if(url == null || url.isEmpty() || id == null || id.isEmpty()) {
			return null;
		}
		url = formatUrl(url);
		log.info("Overwrite saving url {} with id {}", url, id);
		RedirectEntity redirect = redirectDao.findOne(id);
		if(redirect == null) {
			redirect = new RedirectEntity(id, url);
		} else {
			redirect.setRedirectUrl(url);
		}
		redirectDao.save(redirect);
		log.info("Saved for id {}", id);
		return UrlResponse.builder().id(id).url(url).build();
	}
	
	@Override
	public Response save(String id, String url) throws IdGenerationException {
		Response response = null;
		if (StringUtils.isEmpty(id)) {
			response = save(url);
		} else {
			response = saveWithId(id, url);
		}
		return response;
	}
	
	@Transactional
	private Response saveWithId(String id, String url) {
		if(url == null || url.isEmpty() || id == null || id.isEmpty()) {
			return null;
		}
		url = formatUrl(url);
		log.info("Saving url {} with id {}", url, id);
		UrlResponse.UrlResponseBuilder builder = UrlResponse.builder().id(id).url(url);
		RedirectEntity redirect = redirectDao.findOne(id);
		if(redirect == null) {
			redirect = new RedirectEntity(id, url);
			redirectDao.save(redirect);
			log.info("Saved for id {}", id);
		} else {
			log.info("Id '{}' is already used", id);
			builder.error(true);
			builder.message("Id " + id + " is already used.");
		}
		return builder.build();
	}
	
	@Transactional
	private Response save(String url) throws IdGenerationException {
		if(url == null || url.isEmpty()) {
			return null;
		}
		url = formatUrl(url);
		log.info("Saving url {}", url);
		String id = null;
		boolean success = false;
		int count = 1;
		while(!success && count < MAX_ID_GENERATION_TRIES) {
			id = RandomStringUtils.randomAlphanumeric(REDIRECT_ID_LENGTH);
			if(!redirectDao.exists(id)) {
				success = true;
			}
			count++;
		}
		if(!success) {
			log.error("Could not create unused id after {} tries", MAX_ID_GENERATION_TRIES);
			throw new IdGenerationException();
		}
		RedirectEntity redirect = new RedirectEntity(id, url);
		redirectDao.save(redirect);
		log.info("Saved for id {}", id);
		return UrlResponse.builder().id(id).url(url).build();
	}
	
	private String formatUrl(String url) {
		String result = url;
		if(!(url.startsWith("http://") || url.startsWith("https://"))) {
			result = "http://" + url;
		}
		return result;
	}

}
