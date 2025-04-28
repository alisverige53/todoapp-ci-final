package com.example.productservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Product name is required")
    private String name;

    @Min(value = 0, message = "Price must be zero or positive")
    private Double price;

    @Min(value = 0, message = "Stock must be zero or positive")
    private int stock;

    /**
     * VG-förbättring: Förhindra negativt lagervärde
     * Denna metod kastar ett undantag om ett negativt värde försöks sättas
     */
    public void setStock(int stock) {
        if (stock < 0) {
            throw new IllegalArgumentException("Lagervärde får inte vara negativt.");
        }
        this.stock = stock;
    }
}
