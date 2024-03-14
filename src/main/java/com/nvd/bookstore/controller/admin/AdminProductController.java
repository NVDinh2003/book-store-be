package com.nvd.bookstore.controller.admin;

import com.nvd.bookstore.entity.Product;
import com.nvd.bookstore.payload.request.ProductRequest;
import com.nvd.bookstore.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/products")
public class AdminProductController {

    private final Logger logger = LoggerFactory.getLogger(AdminProductController.class);

    @Autowired
    private ProductService productService;

    @PostMapping
    public ResponseEntity<Product> addProduct(@RequestBody ProductRequest productRequest) {
        Product savedProduct = productService.addProduct(productRequest);
        return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
    }

    @PutMapping("{productId}")
    public ResponseEntity<Product> updateProduct(@PathVariable("productId") Long productId, @RequestBody ProductRequest productRequest) {
        //user.setId(userId);
        Product updatedProduct = productService.updateProduct(productId, productRequest);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

    @DeleteMapping("{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable("productId") Long productId) {
        productService.deleteProduct(productId);
        return new ResponseEntity<>("Delete product successfully", HttpStatus.OK);
    }

//    @PatchMapping("{productId}")
//    public ResponseEntity<String> setProductIsDeleted(@PathVariable("productId") Long productId, ) {
//        productService.setProductIsDeleted(productId);
//        return new ResponseEntity<>("Set Product (id=" + productId + "): isDeleted = true", HttpStatus.OK);
//    }
}
