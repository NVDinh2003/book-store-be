package com.nvd.bookstore.controller.web;

import com.nvd.bookstore.entity.Product;
import com.nvd.bookstore.entity.Publisher;
import com.nvd.bookstore.service.PublisherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/publishers")
public class PublisherController {

    @Autowired
    private PublisherService publisherService;

    @GetMapping()
    public ResponseEntity<List<Publisher>> getAllPublishers() {
        return ResponseEntity.ok(publisherService.getAllPublishers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Publisher> getPublisherById(@PathVariable Long id) {
        return ResponseEntity.ok(publisherService.getPublisherById(id));
    }

    @GetMapping("/{id}/books")
    public ResponseEntity<List<Product>> getBooksByPublisherId(@PathVariable Long id) {
        return ResponseEntity.ok(publisherService.getBooksByPublisherId(id));
    }
}
