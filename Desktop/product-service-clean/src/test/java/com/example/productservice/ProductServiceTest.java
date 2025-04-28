package com.example.productservice;

import com.example.productservice.model.Product;
import com.example.productservice.repository.ProductRepository;
import com.example.productservice.service.ProductService;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    private final ProductRepository productRepository = mock(ProductRepository.class);
    private final ProductService productService = new ProductService(productRepository);

    /**
     * Test 1 – Skapa en ny produkt
     * Arrange – Skapa en ny produkt och konfigurera mock
     * Act – Anropa createProduct
     * Assert – Verifiera att rätt produkt returneras och sparas
     */
    @Test
    void createProduct_shouldReturnSavedProduct() {
        // Arrange
        Product input = new Product(null, "Test", 100.0, 10);
        Product saved = new Product(1L, "Test", 100.0, 10);
        when(productRepository.save(input)).thenReturn(saved);

        // Act
        Product result = productService.createProduct(input);

        // Assert
        assertEquals(saved.getId(), result.getId());
        assertEquals(saved.getName(), result.getName());
        verify(productRepository, times(1)).save(input);
    }

    /**
     * Test 2 – Uppdatera lagersaldo
     * Arrange – Produkt finns i databasen
     * Act – Uppdatera lagret
     * Assert – Kontrollera att lagret uppdateras korrekt
     */
    @Test
    void updateStock_shouldUpdateAndReturnProduct_whenFound() {
        // Arrange
        Product existing = new Product(1L, "Product A", 99.0, 5);
        when(productRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(productRepository.save(any(Product.class))).thenAnswer(i -> i.getArgument(0));

        // Act
        Product updated = productService.updateStock(1L, 20);

        // Assert
        assertEquals(20, updated.getStock());
        verify(productRepository).findById(1L);
        verify(productRepository).save(existing);
    }

    /**
     * Test 3 – Hantera fel om produkt inte finns
     * Arrange – Ingen produkt hittas för det ID:t
     * Act – Försök uppdatera lagret
     * Assert – Kontrollera att undantag kastas med rätt meddelande
     */
    @Test
    void updateStock_shouldThrowException_whenProductNotFound() {
        // Arrange
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                productService.updateStock(99L, 50)
        );

        assertTrue(exception.getMessage().contains("Product med ID 99 hittades inte."));
        verify(productRepository).findById(99L);
    }



    /**
     * Test 4 – Förhindra negativt lager
     * Arrange – Skapa en produkt
     * Act – Sätt negativt värde
     * Assert – Kontrollera att IllegalArgumentException kastas
     */
    @Test
    void setStock_shouldThrowException_whenNegative() {
        // Arrange
        Product product = new Product();

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                product.setStock(-5)
        );
    }
}
