package org.jorion.simplesecurity.controller;

import org.jorion.simplesecurity.entity.SecurityUserDetails;
import org.jorion.simplesecurity.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller that will define the path for the page and fill the {@link Model}
 * with what the page will display.
 */
@Controller
public class MainPageController {
	private static final Logger LOG = LoggerFactory.getLogger(MainPageController.class);

	@Autowired
	private ProductService productService;

	@GetMapping("/main")
	public String main(Authentication auth, Model model) {

		String name = logInfo(auth);
		LOG.info("/main, name [{}]", name);
		model.addAttribute("username", name);
		model.addAttribute("products", productService.findAll());
		return "main.html";
	}

	private String logInfo(Authentication auth) {

		if (auth == null) {
			return "???";
		}
		String name = auth.getName();

		Object principal = auth.getPrincipal();
		LOG.info("UserName [{}], Authentication [{}], Principal [{}]", auth.getName(), auth.getClass().getSimpleName(),
				principal.getClass().getSimpleName());
		
		if (principal instanceof DefaultOAuth2User) {
			DefaultOAuth2User user = (DefaultOAuth2User) principal;
			user.getAuthorities().forEach(e -> LOG.debug("Authority [{}]", e.getAuthority()));
			user.getAttributes().forEach((key, value) -> LOG.trace(key + ": " + value));
			name = user.getAttribute("name");
		}

		if (principal instanceof SecurityUserDetails) {
			SecurityUserDetails user = (SecurityUserDetails) principal;
			user.getAuthorities().forEach(e -> LOG.debug("Authority [{}]", e.getAuthority()));
			name = user.getUsername();
		}

		return name;
	}
}
