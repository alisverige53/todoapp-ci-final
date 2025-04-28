package com.example.productservice.controller;

import com.example.productservice.model.Product;
import com.example.productservice.model.ProductRequest;
import com.example.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/products") // Base URL
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;


    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    // @GetMapping("/{id}")
    // public Product getProductById(@PathVariable Long id) {
    //  return productService.getProductById(id)
    //        .orElseThrow(() -> new RuntimeException("Product not found with ID: " + id));
    //  }
    // Förbättrad felhantering:
    // Vi ersatte RuntimeException med ResponseStatusException (HttpStatus.NOT_FOUND)
    // så att användaren får ett korrekt HTTP 404-svar om produkten inte finns.
    // Detta gör API:t mer tydligt och tester blir mer exakta.

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id) {
        return productService.getProductById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Product createProduct(@Valid @RequestBody ProductRequest request) {
        Product product = new Product();
        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        return productService.createProduct(product);
    }



    @PutMapping("/{id}/stock")
    public Product updateStock(@PathVariable Long id, @RequestParam int stock) {
        return productService.updateStock(id, stock);
    }


    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }
}
