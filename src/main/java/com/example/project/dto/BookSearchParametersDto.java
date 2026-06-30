package com.example.project.dto;

public record BookSearchParametersDto(
        String[] titles,
        String[] authors,
        String[] isbns
) {
}
