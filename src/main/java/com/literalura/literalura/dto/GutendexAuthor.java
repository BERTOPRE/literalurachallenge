package com.literalura.literalura.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GutendexAuthor(
        String name,
        Integer birth_year,
        Integer death_year
) {}