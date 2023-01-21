package org.jorion.simplesecurity.controller;

import org.jorion.simplesecurity.service.JwtTokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OAuthController {

	private static final Logger LOG = LoggerFactory.getLogger(OAuthController.class);
	
	@Autowired
	private JwtTokenService tokenService;

	/**
	 * Once the user is authenticated (normally via a request token), we'll generate
	 * and return an access token for him.
	 */
	@PostMapping("/token")
	public String createAccessToken(Authentication auth) {

		LOG.info("Token requested for user [{}]", auth.getName());
		String token = tokenService.generateToken(auth);
		return token;
	}

	@GetMapping("/user")
	public String user(Authentication authentication) {

		return authentication.toString();
	}

}
