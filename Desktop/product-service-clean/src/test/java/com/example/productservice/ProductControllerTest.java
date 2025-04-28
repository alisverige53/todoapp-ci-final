package com.example.productservice.controller;

import com.example.productservice.controller.ProductController;

import com.example.productservice.model.Product;
import com.example.productservice.service.ProductService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
    /**
     * Komponenttest – Testar ProductController med ett mockat service-lager.
     * Vi använder MockMvc för att skicka HTTP-anrop och verifiera respons.
     */
@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    /**
     *  Komponenttest: GET /api/products
     *  Arrange – Mocka två produkter och ställ in produktservicen
     *  Act – Skicka en GET-förfrågan till API:et
     *  Assert – Verifiera att status, innehållstyp och data är korrekt
     */

    @Test
    void getAllProducts_shouldReturnListOfProducts() throws Exception {
        // Arrange
        Product product1 = new Product(1L, "Product A", 99.0, 10);
        Product product2 = new Product(2L, "Product B", 59.0, 5);
        when(productService.getAllProducts()).thenReturn(List.of(product1, product2));

        // Act & Assert
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].name").value("Product A"))
                .andExpect(jsonPath("$[1].stock").value(5));

        // Verifiera att tjänsten anropades exakt en gång
        verify(productService, times(1)).getAllProducts();
    }
}
