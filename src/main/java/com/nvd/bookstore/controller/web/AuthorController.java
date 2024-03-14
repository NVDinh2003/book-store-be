package com.nvd.bookstore.controller.web;

import com.nvd.bookstore.controller.admin.AdminAuthorController;
import com.nvd.bookstore.entity.Author;
import com.nvd.bookstore.entity.Product;
import com.nvd.bookstore.service.AuthorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/authors")
public class AuthorController {
    private final Logger logger = LoggerFactory.getLogger(AdminAuthorController.class);

    @Autowired
    private AuthorService authorService;

    @GetMapping("")
    public ResponseEntity<List<Author>> getAllAuthors() {
        return ResponseEntity.ok(authorService.getAllAuthors());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Author> getAuthorById(@PathVariable Long id) {
        return ResponseEntity.ok(authorService.getAuthorById(id));
    }

    @GetMapping("/{id}/books")
    public ResponseEntity<List<Product>> getBooksByAuthorId(@PathVariable Long id) {
        return ResponseEntity.ok(authorService.getBooksByAuthorId(id));
    }

}
