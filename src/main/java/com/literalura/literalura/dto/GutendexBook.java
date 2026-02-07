package com.literalura.literalura.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GutendexBook(
        String title,
        List<GutendexAuthor> authors,
        List<String> languages,
        Integer download_count
) {}