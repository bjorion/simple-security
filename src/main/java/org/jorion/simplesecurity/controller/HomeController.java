package org.jorion.simplesecurity.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String home() {

        return "Hello, home";
    }

    @GetMapping("/write")
    public String write() {

        return "You need write access to reach that point";
    }

    @GetMapping("/public")
    public ResponseEntity<String> publicHome() {

        return ResponseEntity.ok("This response does not require the user to be authenticated");
    }
}
