package com.example.productservice;

import com.example.productservice.controller.ProductController;
import com.example.productservice.model.Product;
import com.example.productservice.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.server.ResponseStatusException;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ProductControllerVGTest {

    private MockMvc mockMvc;

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(productController)
                .setControllerAdvice(new com.example.productservice.service.exception.GlobalExceptionHandler())
                .build();
    }

    /**
     * Test 1 – Kontrollera att 404 returneras när produkten inte hittas
     * Arrange – Mocka productService att kasta ResponseStatusException
     * Act – Skicka GET /api/products/99
     * Assert – Förvänta oss HTTP 404
     */
    @Test
    void getProductById_shouldReturn404_whenProductNotFound() throws Exception {
        // Arrange
        when(productService.getProductById(99L))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found with ID: 99"));

        // Act & Assert
        mockMvc.perform(get("/api/products/99"))
                .andExpect(status().isNotFound());

        verify(productService, times(1)).getProductById(99L);
    }

    /**
     * Test 2 – Kontrollera att 400 returneras vid ogiltig POST
     * Arrange – Skapa ogiltig produkt JSON
     * Act – Skicka POST /api/products med invalid data
     * Assert – Förvänta oss HTTP 400
     */
    @Test
    void createProduct_shouldReturnBadRequest_whenInvalidInput() throws Exception {
        // Arrange
        String invalidJson = """
            {
                "name": "",
                "price": 100.0,
                "stock": -10
            }
            """;

        // Act & Assert
        mockMvc.perform(post("/api/products")
                        .contentType("application/json")
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
    }
}
