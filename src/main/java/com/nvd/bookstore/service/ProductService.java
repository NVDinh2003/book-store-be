package com.nvd.bookstore.service;

import com.nvd.bookstore.entity.Product;
import com.nvd.bookstore.payload.request.ProductRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {
    Page<Product> findAll(Pageable pageable);

//    Optional<Product> findById(Long id);

    Product getProductById(Long productId);

    Product addProduct(ProductRequest product);

    Product updateProduct(Long productId, ProductRequest productRequest);

    void deleteProduct(Long productId);

    void setProductIsDeleted(Long productId);

    Page<Product> searchProduct(String keyWord, Pageable pageable);

    List<Product> top10BestSeller();

    List<Product> top10LatestBooks();


}
