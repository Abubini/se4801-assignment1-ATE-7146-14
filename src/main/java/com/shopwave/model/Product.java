//Biniyam Girma ATE//176/14
package com.shopwave.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import org.hibernate.annotations.CreationTimestamp;
import java.math.BigDecimal;
import java.time.LocalDateTime;


@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;

    @Positive
    @Column(nullable = false)
    private BigDecimal price;

    @Min(0)
    private Integer stock;

    @ManyToOne
    private Category category;

    @CreationTimestamp
    private LocalDateTime createdAt;
}