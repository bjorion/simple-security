package org.jorion.simplesecurity.controller;

import lombok.extern.slf4j.Slf4j;
import org.jorion.simplesecurity.entity.SecurityUserDetails;
import org.jorion.simplesecurity.service.ProductService;
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
@Slf4j
@Controller
public class MainPageController {

    @Autowired
    private ProductService productService;

    @GetMapping("/main")
    public String main(Authentication auth, Model model) {

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
                principal.getClass().getSimpleName());

        if (principal instanceof DefaultOAuth2User user) {
            user.getAuthorities().forEach(e -> log.debug("Authority [{}]", e.getAuthority()));
            user.getAttributes().forEach((key, value) -> log.trace(key + ": " + value));
            name = user.getAttribute("name");
        } else if (principal instanceof SecurityUserDetails user) {
            user.getAuthorities().forEach(e -> log.debug("Authority [{}]", e.getAuthority()));
            name = user.getUsername();
        } else {
            name = auth.getName();
        }

        return name;
    }
}
