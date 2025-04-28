package com.example.productservice.service;

import com.example.productservice.model.Product;
import com.example.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    // Hämta alla produkter
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // Hämta produkt med ID
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    // Skapa en ny produkt
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    // Ta bort en produkt
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    // Uppdatera lagersaldo för en produkt
   // public Product updateStock(Long id, int newStock) {
     //   return productRepository.findById(id).map(product -> {
       //     product.setStock(newStock);
         //   return productRepository.save(product);
        //}).orElseThrow(() -> new RuntimeException("Product not found with ID: " + id));
    // }


    //  VG-förbättring: Förhindra att lagret uppdateras med ett negativt värde
// Detta är en säkerhetskontroll för att undvika logiska fel i affärslogiken
// Vi använder ett if-villkor för att kasta ett undantag om det nya lagervärdet är mindre än 0

    public Product updateStock(Long id, int newStock) {
        if (newStock < 0) {
            throw new IllegalArgumentException("Lagervärde får inte vara negativt.");
        }

        return productRepository.findById(id)
                .map(product -> {
                    product.setStock(newStock);
                    return productRepository.save(product);
                })
                .orElseThrow(() -> new RuntimeException("Product med ID " + id + " hittades inte."));

    }

}