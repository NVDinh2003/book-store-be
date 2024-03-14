package com.nvd.bookstore.controller.admin;

import com.nvd.bookstore.entity.Publisher;
import com.nvd.bookstore.payload.request.PublisherRequest;
import com.nvd.bookstore.service.PublisherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/publishers")
public class AdminPublisherController {

    @Autowired
    private PublisherService publisherService;

    @PostMapping("")
    public ResponseEntity<Publisher> addAuthor(@RequestBody PublisherRequest publisherRequest) {
        return ResponseEntity.ok(publisherService.addPublisher(publisherRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Publisher> updateAuthor(@PathVariable Long id, @RequestBody PublisherRequest publisherRequest) {
        return ResponseEntity.ok(publisherService.updatePublisher(id, publisherRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAuthor(@PathVariable Long id) {
        publisherService.deletePublisher(id);
        return new ResponseEntity<>("Delete publisher successfully", HttpStatus.OK);
    }
}
