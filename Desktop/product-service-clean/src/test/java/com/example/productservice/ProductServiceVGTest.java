package com.example.productservice;

import com.example.productservice.model.Product;
import com.example.productservice.repository.ProductRepository;
import com.example.productservice.service.ProductService;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * VG-test: Testar logik med if-villkor och edge case för negativt lager.
 * Säkerställer att ett undantag kastas om lagret är mindre än 0.
 */
@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class) // Viktigt för att köra tester i ordning
public class ProductServiceVGTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    /**
     * Test 1 - Ska kasta IllegalArgumentException vid negativt lagervärde
     * Arrange - Skapar och sparar en produkt
     * Act - Försöker uppdatera med ett negativt lager
     * Assert - Verifierar att korrekt undantag kastas
     */
    @Test
    @Order(1)
    void shouldThrowExceptionForNegativeStock() {
        // Arrange
        Product produkt = new Product(null, "VG-Test", 300.0, 10);
        Product sparad = productRepository.save(produkt);
        int ogiltigtLager = -5;

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> productService.updateStock(sparad.getId(), ogiltigtLager)
        );

        // Assert
        assertEquals("Lagervärde får inte vara negativt.", exception.getMessage());
    }

    /**
     * Test 2 - Ska uppdatera lagervärde korrekt vid positivt värde
     * Arrange - Skapar och sparar en produkt med giltigt lager
     * Act - Uppdaterar lagret
     * Assert - Verifierar att lagret och övriga fält är korrekta
     */
    @Test
    @Order(2)
    void shouldUpdateStockSuccessfully() {
        // Arrange
        Product sparad = productRepository.save(
                new Product(null, "VG Success", 150.0, 20)
        );
        int nyttLager = 30;

        // Act
        Product uppdaterad = productService.updateStock(sparad.getId(), nyttLager);

        // Assert
        assertEquals(nyttLager, uppdaterad.getStock());
        assertEquals("VG Success", uppdaterad.getName());
        assertEquals(150.0, uppdaterad.getPrice());
    }
}

