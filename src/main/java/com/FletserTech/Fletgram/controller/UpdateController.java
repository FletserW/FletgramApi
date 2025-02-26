package com.FletserTech.Fletgram.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/update")
public class UpdateController {

    @GetMapping("/latest")
    public ResponseEntity<Map<String, String>> getLatestVersion() {
        Map<String, String> response = new HashMap<>();
        response.put("version", "1.0.5"); // Número da versão atual
        response.put("url", "https://fletgram.loca.lt/static/apk/fletgram_1.1.0.apk"); // Link do APK
        return ResponseEntity.ok(response);
    }
}

