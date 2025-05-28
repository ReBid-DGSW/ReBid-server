package com.retrade.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "products")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "original_price", precision = 10, scale = 2)
    private BigDecimal originalPrice;

    private Integer discount;

    @Column(name = "product_condition", nullable = false)
    private String condition;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ElementCollection
    @CollectionTable(name = "product_images", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "image_url")
    private List<String> images;

    @Column(nullable = false)
    private String category;

    private String subcategory;

    private String location;

    @Column(name = "upload_date")
    @Builder.Default
    private LocalDateTime uploadDate = LocalDateTime.now();

    @Builder.Default
    private Integer views = 0;

    @Builder.Default
    private Integer likes = 0;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Status status = Status.AVAILABLE;

    @Column(name = "created_at")
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", nullable = false)
    private User seller;

    // Remove or comment out the following lines if ProductSpec, ShippingOption, ProductLike do not exist
    // Or create stub classes for them in the correct package

    // @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    // private List<ProductSpec> specs;

    // @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    // private List<ShippingOption> shippingOptions;

    // @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    // private List<ProductLike> productLikes;

    public enum Status {
        AVAILABLE, SOLD, RESERVED
    }
}