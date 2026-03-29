//Biniyam Girma ATE//176/14
package com.shopwave.controller;

import com.shopwave.dto.ProductDTO;
import com.shopwave.exception.ProductNotFoundException;
import com.shopwave.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Test
    void createProduct_shouldReturn201() throws Exception {
        String json = """
        {
          "name": "Phone",
          "price": 500,
          "stock": 10
        }
        """;

        ProductDTO dto = ProductDTO.builder()
                .id(1L)
                .name("Phone")
                .build();

        when(productService.createProduct(any())).thenReturn(dto);

        mockMvc.perform(post("/api/products")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Phone"));
    }

    @Test
    void getProductById_shouldReturn200() throws Exception {
        ProductDTO dto = ProductDTO.builder()
                .id(1L)
                .name("Laptop")
                .price(BigDecimal.valueOf(1000))
                .stock(5)
                .build();

        when(productService.getProductById(1L)).thenReturn(dto);

        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Laptop"));
    }

    @Test
    void getProductById_notFound_shouldReturn404() throws Exception {
        when(productService.getProductById(99L))
                .thenThrow(new ProductNotFoundException(99L));

        mockMvc.perform(get("/api/products/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void createProduct_invalid_shouldReturn400() throws Exception {
        String json = """
        {
          "name": "",
          "price": -10
        }
        """;

        mockMvc.perform(post("/api/products")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isBadRequest());
    }


    @Test
    void updateStock_shouldReturn200() throws Exception {
        String json = """
        {
          "delta": -2
        }
        """;

        ProductDTO dto = ProductDTO.builder()
                .id(1L)
                .stock(8)
                .build();

        when(productService.updateStock(eq(1L), eq(-2))).thenReturn(dto);

        mockMvc.perform(patch("/api/products/1/stock")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isOk());
    }


    @Test
    void updateStock_negative_shouldReturn400() throws Exception {
        String json = """
        {
          "delta": -100
        }
        """;

        when(productService.updateStock(eq(1L), eq(-100)))
                .thenThrow(new IllegalArgumentException("Stock cannot be negative"));

        mockMvc.perform(patch("/api/products/1/stock")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isBadRequest());
    }


    @Test
    void searchProducts_shouldReturnList() throws Exception {
        mockMvc.perform(get("/api/products/search")
                        .param("keyword", "lap"))
                .andExpect(status().isOk());
    }


}