package com.nvd.bookstore.controller.web;

import com.nvd.bookstore.entity.Category;
import com.nvd.bookstore.entity.Product;
import com.nvd.bookstore.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping()
    public ResponseEntity<List<Category>> getAllCategories() {
        return new ResponseEntity<>(categoryService.getAll(), HttpStatus.OK);
    }

    @GetMapping("{categoryId}")
    public ResponseEntity<Category> getByID(@PathVariable("categoryId") Long categoryId) {
        return new ResponseEntity<>(categoryService.getById(categoryId), HttpStatus.OK);
    }

    @GetMapping("{categoryId}/products")
    public ResponseEntity<List<Product>> getProductsByCategory(@PathVariable("categoryId") Long categoryId) {
        return new ResponseEntity<>(categoryService.getProductsByCategory(categoryId), HttpStatus.OK);
    }

}
