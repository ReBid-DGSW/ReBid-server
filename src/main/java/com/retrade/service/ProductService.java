package com.retrade.service;

import com.retrade.entity.Product;
import com.retrade.entity.User;
import com.retrade.repository.ProductRepository;
import com.retrade.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public Product createProduct(Product product, String userEmail) {
        User seller = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        product.setSeller(seller);
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());

        return productRepository.save(product);
    }

    @Transactional(readOnly = true)
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다."));
    }

    @Transactional(readOnly = true)
    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Page<Product> searchProducts(String keyword, Pageable pageable) {
        return productRepository.findByKeyword(keyword, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Product> getProductsByCategory(String category, Pageable pageable) {
        return productRepository.findByCategory(category, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Product> getProductsBySeller(Long sellerId, Pageable pageable) {
        return productRepository.findBySellerId(sellerId, pageable);
    }

    @Transactional(readOnly = true)
    public List<Product> getRecentProducts(int limit) {
        return productRepository.findRecentProducts(Pageable.ofSize(limit));
    }

    public Product updateProduct(Long id, Product updatedProduct, String userEmail) {
        Product product = getProductById(id);
        
        // 권한 체크
        if (!product.getSeller().getEmail().equals(userEmail)) {
            throw new RuntimeException("상품을 수정할 권한이 없습니다.");
        }

        product.setName(updatedProduct.getName());
        product.setDescription(updatedProduct.getDescription());
        product.setCategory(updatedProduct.getCategory());
        product.setCondition(updatedProduct.getCondition());
        product.setImages(updatedProduct.getImages());
        product.setUpdatedAt(LocalDateTime.now());

        return productRepository.save(product);
    }

    public void deleteProduct(Long id, String userEmail) {
        Product product = getProductById(id);
        
        // 권한 체크
        if (!product.getSeller().getEmail().equals(userEmail)) {
            throw new RuntimeException("상품을 삭제할 권한이 없습니다.");
        }

        productRepository.delete(product);
    }
}
