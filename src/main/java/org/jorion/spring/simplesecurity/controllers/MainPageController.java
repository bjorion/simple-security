package org.jorion.spring.simplesecurity.controllers;

import org.jorion.spring.simplesecurity.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller that will define the path for the page and fill the {@link Model} with what the page will display.
 */
@Controller
public class MainPageController
{
	@Autowired
	private ProductService productService;

	// --- Methods ---
	@GetMapping("/main")
	public String main(Authentication a, Model model)
	{
		model.addAttribute("username", (a == null) ? "???" : a.getName());
		model.addAttribute("products", productService.findAll());
		return "main.html";
	}
}
