package com.literalura.literalura.service;

import com.literalura.literalura.dto.GutendexResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GutendexService {

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String URL = "https://gutendex.com/books?search=";

    public GutendexResponse searchBooks(String title) {
        return restTemplate.getForObject(URL + title, GutendexResponse.class);
    }
}
