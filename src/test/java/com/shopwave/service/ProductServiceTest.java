//Biniyam Girma ATE//176/14
package com.shopwave.service;

import com.shopwave.dto.CreateProductRequest;
import com.shopwave.model.Product;
import com.shopwave.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    void createProduct_shouldReturnProductDTO() {
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
}