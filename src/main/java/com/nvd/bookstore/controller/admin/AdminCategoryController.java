package com.nvd.bookstore.controller.admin;

import com.nvd.bookstore.entity.Category;
import com.nvd.bookstore.payload.request.CategoryRequest;
import com.nvd.bookstore.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/categories")
public class AdminCategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping()
    public ResponseEntity<Category> addCategory(@RequestBody CategoryRequest categoryRequest) {
        return new ResponseEntity<>(categoryService.addCategory(categoryRequest), HttpStatus.OK);
    }

    @PutMapping("{categoryId}")
    public ResponseEntity<Category> updateCategory(@PathVariable("categoryId") Long categoryId,
                                                   @RequestBody CategoryRequest categoryRequest) {
        return new ResponseEntity<>(categoryService.updateCategory(categoryId, categoryRequest), HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public void deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
    }
}
