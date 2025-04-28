package com.example.productservice;
//  testverktyg från JUnit.
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
//  Java-verktyg för att jobba med listor.
import java.util.Arrays;
import java.util.List;
//  statiska metoder från JUnit för att göra assertion.
import static org.junit.jupiter.api.Assertions.*;

/**
 * Integrationstest för ProductService
 * Vi testar hela flödet från HTTP-anrop till databas med en riktig MySQL-databas.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-test.properties")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProductIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void setup() {
        // Arrange - Rensa databasen före varje test
        productRepository.deleteAll();
    }

    /**
     * Test 1 - Skapa en ny produkt via API
     */
    @Test
    @Order(1)
    void shouldCreateNewProduct() {
        // Arrange - Förbered en ny produkt
        Product product = new Product(null, "Integration Product", 200.0, 15);

        // Act - Skicka POST-förfrågan till /api/products
        ResponseEntity<Product> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/products",
                product,
                Product.class
        );

        // Assert - Verifiera resultatet
        assertEquals(HttpStatus.CREATED, response.getStatusCode(), "Status ska vara 201 CREATED");
        assertNotNull(response.getBody(), "Responsen får inte vara null");

        Product createdProduct = response.getBody();
        assertEquals("Integration Product", createdProduct.getName());
        assertEquals(200.0, createdProduct.getPrice());
        assertEquals(15, createdProduct.getStock());
    }

    /**
     * Test 2 - Hämta alla produkter från API
     */
    @Test
    @Order(2)
    void shouldReturnListOfProducts() {
        // Arrange - Lägg till två produkter i databasen
        productRepository.save(new Product(null, "P1", 100.0, 10));
        productRepository.save(new Product(null, "P2", 150.0, 5));

        // Act - Skicka GET-förfrågan till /api/products
        ResponseEntity<Product[]> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/api/products",
                Product[].class
        );

        // Assert - Kontrollera svar och innehåll
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Status ska vara 200 OK");
        assertNotNull(response.getBody(), "Responsen får inte vara null");

        List<Product> products = Arrays.asList(response.getBody());
        assertEquals(2, products.size(), "Vi förväntar oss två produkter");
    }

    /**
     * Test 3 - Skriv ut alla produkter i databasen
     * (Endast för konsolvisning - inget assert krav)
     */
    @Test
    @Order(3)
    void printAllProducts() {
        // Act - Hämta alla produkter
        List<Product> products = productRepository.findAll();

        // Konsolutskrift
        System.out.println("-- Lista över alla produkter i databasen --");
        if (products.isEmpty()) {
            System.out.println("Inga produkter hittades i databasen.");
        } else {
            products.forEach(p -> System.out.println(
                    p.getId() + ": " +
                            p.getName() + ", pris: " +
                            p.getPrice() + ", antal: " +
                            p.getStock()
            ));
        }
    }


}