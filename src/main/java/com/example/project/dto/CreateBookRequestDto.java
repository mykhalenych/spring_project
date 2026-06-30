package com.example.project.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class CreateBookRequestDto {
    @NotBlank
    @Size(max = 255)
    private String title;

    @NotBlank
    @Size(max = 255)
    private String author;

    @NotBlank
    @Pattern(regexp = "^[0-9Xx-]{10,17}$", message = "must be a valid ISBN format")
    private String isbn;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = true)
    private BigDecimal price;

    @Size(max = 1000)
    private String description;

    @Size(max = 255)
    private String coverImage;
}
