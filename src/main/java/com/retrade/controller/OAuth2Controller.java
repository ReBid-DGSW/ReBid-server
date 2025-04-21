package com.retrade.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/oauth2")
@CrossOrigin(origins = "*")
public class OAuth2Controller {

    @GetMapping("/authorization/{provider}")
    public ResponseEntity<Map<String, String>> getAuthorizationUrl(@PathVariable String provider) {
        Map<String, String> response = new HashMap<>();
        
        String authUrl = "/oauth2/authorization/" + provider.toLowerCase();
        response.put("authUrl", authUrl);
        response.put("provider", provider);
        
        return ResponseEntity.ok(response);
    }
}
