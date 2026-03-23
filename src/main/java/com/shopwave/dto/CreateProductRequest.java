package com.shopwave.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateProductRequest {

    @NotBlank
    private String name;

    private String description;

    @Positive
    private BigDecimal price;

    @Min(0)
    private Integer stock;

    private Long categoryId;
}