package org.jorion.simplesecurity.controller;

import lombok.extern.slf4j.Slf4j;
import org.jorion.simplesecurity.service.JwtTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class OAuthController {

    @Autowired
    private JwtTokenService tokenService;

    /**
     * Once the user is authenticated (normally via a request token), we'll generate
     * and return an access token for him.
     */
    @PostMapping("/token")
    public String createAccessToken(Authentication auth) {

        log.info("Token requested for user [{}]", auth.getName());
        return tokenService.generateToken(auth);
    }

    @GetMapping(value = "/user", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> user(Authentication authentication) {

        String body = authentication.toString();
        return ResponseEntity.ok(body);
    }

}
