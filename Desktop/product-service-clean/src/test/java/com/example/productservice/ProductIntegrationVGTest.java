package com.example.productservice;

import com.example.productservice.model.Product;
import com.example.productservice.repository.ProductRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integrationstest med avancerade kontroller
 * Dessa tester körs mot en riktig MySQL-databas (product_db) på localhost.
 * Vi testar hela applikationsflödet: HTTP-anrop, service, repository och databasinteraktion.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-test.properties")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProductIntegrationVGTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ProductRepository productRepository;

    /**
     * Test 1 - Skapa en ny produkt via API
     * AAA:
     * Arrange - Skapa en ny produkt
     * Act - Skicka POST-förfrågan
     * Assert - Kontrollera status och produktdata
     */
    @Test
    @Order(1)
    void shouldCreateNewProduct() {
        Product product = new Product(null, "VG Product", 300.0, 20);

        ResponseEntity<Product> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/products",
                product,
                Product.class
        );

        assertEquals(HttpStatus.CREATED, response.getStatusCode(), "Status ska vara 201 CREATED");
        assertNotNull(response.getBody(), "Responsen får inte vara null");

        Product created = response.getBody();
        assertEquals("VG Product", created.getName());
        assertTrue(created.getPrice() > 0, "Priset måste vara positivt");
        assertTrue(created.getStock() >= 0, "Lagret får inte vara negativt");

        // Kontroll med if-sats för VG-nivå
        if (!created.getName().startsWith("VG")) {
            fail("Produktens namn ska börja med 'VG'");
        }
    }

    /**
     * Test 2 - Hämta alla produkter via GET
     * AAA:
     * Arrange - Lägg till två produkter i databasen
     * Act - Skicka GET-förfrågan
     * Assert - Kontrollera svar och produktdata
     */
    @Test
    @Order(2)
    void shouldReturnListOfProducts() {
        // Arrange - Lägg till testprodukter
        productRepository.save(new Product(null, "Test1", 120.0, 8));
        productRepository.save(new Product(null, "Test2", 220.0, 3));

        // Act - Skicka GET till /api/products
        ResponseEntity<Product[]> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/api/products",
                Product[].class
        );

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        List<Product> products = Arrays.asList(response.getBody());
        assertTrue(products.size() >= 2, "Minst 2 produkter förväntas");

        for (Product p : products) {
            if (p.getName() == null || p.getName().isEmpty()) {
                fail("Produktens namn får inte vara tomt eller null");
            }
        }
    }

    /**
     * Test 3 - Lista alla produkter i databasen efter test
     * Endast för visning i konsolen
     * AAA:
     * Arrange - (ingen extra setup behövs)
     * Act - Hämta alla produkter
     * Assert - Databasen ska inte vara tom
     */
    @Test
    @Order(3)
    void printAllProducts() {
        // Act - Hämta produkter
        List<Product> products = productRepository.findAll();

        // Visa i konsolen
        System.out.println("-- Lista av produkter i databasen --");
        products.forEach(p -> System.out.println(
                p.getId() + " | " + p.getName() + " | Pris: " +
                        p.getPrice() + " | Lager: " + p.getStock()
        ));

        // Assert
        assertFalse(products.isEmpty(), "Databasen får inte vara tom");
    }
}
