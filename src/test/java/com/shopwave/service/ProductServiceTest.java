//Biniyam Girma ATE//176/14
package com.shopwave.service;

import com.shopwave.dto.CreateProductRequest;
import com.shopwave.exception.ProductNotFoundException;
import com.shopwave.model.Product;
import com.shopwave.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    void createProduct() {
        // this should return a ProductDTO with the same name as the created product ("Phone")
        CreateProductRequest request = new CreateProductRequest();
        request.setName("Phone");
        request.setPrice(BigDecimal.valueOf(500));
        request.setStock(10);

        Product saved = Product.builder()
                .id(1L)
                .name("Phone")
                .price(BigDecimal.valueOf(500))
                .stock(10)
                .build();

        when(productRepository.save(any(Product.class))).thenReturn(saved);

        var result = productService.createProduct(request);

        assertNotNull(result);
        assertEquals("Phone", result.getName());
    }

    @Test
    void getProductByIdNotFound() {
        // this should return an exception when the product with the given ID is not found
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () ->
                productService.getProductById(1L));
    }

    @Test
    void updateStockNegative() {
        // this should return an exception when updating stock results in a negative value
        Product product = Product.builder()
                .id(1L)
                .stock(5)
                .build();

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        assertThrows(IllegalArgumentException.class, () ->
                productService.updateStock(1L, -10));
    }


    @Test
    void searchProducts() {
        // this should return a non-empty list of products matching the search keyword "lap"
        Product p = Product.builder()
                .name("Laptop")
                .price(BigDecimal.valueOf(1000))
                .build();

        when(productRepository.findByNameContainingIgnoreCase("lap"))
                .thenReturn(List.of(p));

        var result = productService.searchProducts("lap", null);

        assertFalse(result.isEmpty());
    }
}