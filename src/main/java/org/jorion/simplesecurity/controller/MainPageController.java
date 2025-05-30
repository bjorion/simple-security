package org.jorion.simplesecurity.controller;

import lombok.extern.slf4j.Slf4j;
import org.jorion.simplesecurity.entity.SecurityUser;
import org.jorion.simplesecurity.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller that will define the path for the page and fill the {@link Model}
 * with what the page will display.
 */
@Slf4j
@Controller
public class MainPageController {

    @Autowired
    private ProductService productService;

    @GetMapping("/main")
    public String mainPage(Authentication auth, Model model) {

        String name = logInfo(auth);
        log.info("/main, name [{}]", name);
        model.addAttribute("username", name);
        model.addAttribute("products", productService.findAll());
        return "main.html";
    }

    private String logInfo(Authentication auth) {

        if (auth == null) {
            return "???";
        }

        String name;
        Object principal = auth.getPrincipal();
        log.info("UserName [{}], Authentication [{}], Principal [{}]", auth.getName(), auth.getClass().getSimpleName(),
                principal.getClass().getName());

        // OAuth2 User
        switch (principal) {
            case DefaultOAuth2User user -> {
                user.getAuthorities().forEach(e -> log.debug("OAuth2User: Authority [{}]", e.getAuthority()));
                user.getAttributes().forEach((key, value) -> log.trace(key + ": " + value));
                name = user.getAttribute("name");
            }
            // Security User
            case SecurityUser user -> {
                user.getAuthorities().forEach(e -> log.debug("SecurityUser: Authority [{}]", e.getAuthority()));
                name = user.getUsername();
            }
            // Spring User
            case User user -> {
                user.getAuthorities().forEach(e -> log.debug("SpringUser: Authority [{}]", e.getAuthority()));
                name = user.getUsername();
            }
            // Else
            default -> name = auth.getName();
        }

        return name;
    }
}
