package com.shopwave.service;

import com.shopwave.dto.CreateProductRequest;
import com.shopwave.dto.ProductDTO;
import com.shopwave.exception.ProductNotFoundException;
import com.shopwave.mapper.ProductMapper;
import com.shopwave.model.Product;
import com.shopwave.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public ProductDTO createProduct(CreateProductRequest request) {
        Product product = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .stock(request.getStock())
                .build();

        return ProductMapper.toDTO(productRepository.save(product));
    }

    @Override
    @Transactional(Transactional.TxType.SUPPORTS)
    public Page<ProductDTO> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(ProductMapper::toDTO);
    }

    @Override
    public ProductDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        return ProductMapper.toDTO(product);
    }

    @Override
    public List<ProductDTO> searchProducts(String keyword, BigDecimal maxPrice) {
        if (keyword != null && maxPrice != null) {
            return productRepository.findByNameContainingIgnoreCase(keyword)
                    .stream()
                    .filter(p -> p.getPrice().compareTo(maxPrice) <= 0)
                    .map(ProductMapper::toDTO)
                    .toList();
        } else if (keyword != null) {
            return productRepository.findByNameContainingIgnoreCase(keyword)
                    .stream()
                    .map(ProductMapper::toDTO)
                    .toList();
        } else if (maxPrice != null) {
            return productRepository.findByPriceLessThanEqual(maxPrice)
                    .stream()
                    .map(ProductMapper::toDTO)
                    .toList();
        } else {
            return productRepository.findAll()
                    .stream()
                    .map(ProductMapper::toDTO)
                    .toList();
        }
    }

    @Override
    public ProductDTO updateStock(Long id, int delta) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        int newStock = product.getStock() + delta;

        if (newStock < 0) {
            throw new IllegalArgumentException("Stock cannot be negative");
        }

        product.setStock(newStock);
        return ProductMapper.toDTO(productRepository.save(product));
    }
}