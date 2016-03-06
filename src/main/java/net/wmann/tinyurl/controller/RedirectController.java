package net.wmann.tinyurl.controller;

import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;
import net.wmann.tinyurl.exception.IdGenerationException;
import net.wmann.tinyurl.exception.UrlNotFoundException;
import net.wmann.tinyurl.model.Response;
import net.wmann.tinyurl.service.RedirectService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@Slf4j
public class RedirectController {

	private final RedirectService redirectService;

	@Autowired
	public RedirectController(RedirectService redirectService) {
		this.redirectService = redirectService;
	}

	@RequestMapping(value = "/p/add/", method = RequestMethod.POST)
	public ResponseEntity<Response> addUrl(@RequestParam(value = "url") String url,
			@RequestParam(value = "id", required = false) String id) throws IdGenerationException {
		log.info("Received request to addUrlWithId with url {} and id {}", url, id);
		return new ResponseEntity<>(redirectService.save(id, url), HttpStatus.OK);
	}

	@RequestMapping(value = "/p/add/overwrite/", method = RequestMethod.POST)
	public ResponseEntity<Response> addUrlWithIdOverwrite(@RequestParam(value = "url") String url,
			@RequestParam(value = "id") String id) {
		log.info("Received request to addUrlWithIdOverwrite with url {} and id {}", url, id);
		return new ResponseEntity<>(redirectService.saveOverwrite(id, url), HttpStatus.OK);
	}

	@RequestMapping(value = "/r/{id}", method = RequestMethod.GET)
	public ModelAndView redirect(@PathVariable String id, HttpServletResponse response) throws UrlNotFoundException {
		log.info("Received request to redirect with id {}", id);
		return new ModelAndView("redirect:" + redirectService.findRedirectUrl(id));
	}

}
