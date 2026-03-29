//Biniyam Girma ATE//176/14
package com.shopwave.repository;

import com.shopwave.model.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;


    @Test
    void findByNameContainingIgnoreCase_shouldReturnMatchingProducts() {
        Product p = Product.builder()
                .name("Laptop")
                .price(BigDecimal.valueOf(1000))
                .stock(5)
                .build();

        productRepository.save(p);

        List<Product> result =
                productRepository.findByNameContainingIgnoreCase("lap");

        assertFalse(result.isEmpty());
        assertEquals("Laptop", result.get(0).getName());
    }


    @Test
    void findByPriceLessThanEqual_shouldFilterCorrectly() {
        productRepository.save(Product.builder()
                .name("Cheap")
                .price(BigDecimal.valueOf(50))
                .stock(10)
                .build());

        productRepository.save(Product.builder()
                .name("Expensive")
                .price(BigDecimal.valueOf(500))
                .stock(10)
                .build());

        List<Product> result =
                productRepository.findByPriceLessThanEqual(BigDecimal.valueOf(100));

        assertEquals(1, result.size());
        assertEquals("Cheap", result.get(0).getName());
    }


    @Test
    void findTopByOrderByPriceDesc_shouldReturnMostExpensive() {
        productRepository.save(Product.builder()
                .name("Low")
                .price(BigDecimal.valueOf(100))
                .stock(5)
                .build());

        productRepository.save(Product.builder()
                .name("High")
                .price(BigDecimal.valueOf(1000))
                .stock(5)
                .build());

        Optional<Product> result =
                productRepository.findTopByOrderByPriceDesc();

        assertTrue(result.isPresent());
        assertEquals("High", result.get().getName());
    }
}