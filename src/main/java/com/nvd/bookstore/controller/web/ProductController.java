package com.nvd.bookstore.controller.web;

import com.nvd.bookstore.entity.Product;
import com.nvd.bookstore.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService productService;

//    @GetMapping
//    public List<Product> getAllProducts() {
//        return productService.findAll();
//    }

    @GetMapping
    public ResponseEntity<Page<Product>> getAllProducts(
            @RequestParam(defaultValue = "0", required = false) int pageNumber,
            @RequestParam(defaultValue = "10", required = false) int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Product> productList = productService.findAll(pageable);
//        logger.info("Request to get product: " + productList.getContent().get(0));
        return new ResponseEntity<>(productList, HttpStatus.OK);
    }

    @GetMapping("{productId}")
    public ResponseEntity<Product> getProductByID(@PathVariable("productId") Long productId) {
        return new ResponseEntity<>(productService.getProductById(productId), HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<Product>> searchProduct(@RequestParam("keyWord") String keyWord,
                                                       @RequestParam(defaultValue = "0", required = false) int pageNumber,
                                                       @RequestParam(defaultValue = "10", required = false) int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return new ResponseEntity<>(productService.searchProduct(keyWord, pageable), HttpStatus.OK);
    }

    @GetMapping("/best-seller")
    public ResponseEntity<List<Product>> top10BestSeller() {
        return new ResponseEntity<>(productService.top10BestSeller(), HttpStatus.OK);
    }

    @GetMapping("/latest")
    public ResponseEntity<List<Product>> top10LatestBooks() {
        return new ResponseEntity<>(productService.top10LatestBooks(), HttpStatus.OK);
    }


//    @PostMapping("/test-request")
//    public ResponseEntity<String> testPostRequest() {
//        return ResponseEntity.ok("POST request successful");
//    }
}
