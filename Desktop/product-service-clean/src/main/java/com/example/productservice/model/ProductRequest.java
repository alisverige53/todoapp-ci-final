package com.example.productservice.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Setter;

/**
 * DTO-klass för att skapa nya produkter.
 * Används för att validera inkommande POST-data.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {

    @NotBlank(message = "Product name is required")
    private String name;

    @Min(value = 0, message = "Price must be zero or positive")
    private Double price;

    @Min(value = 0, message = "Stock must be zero or positive")
    private int stock;
}
