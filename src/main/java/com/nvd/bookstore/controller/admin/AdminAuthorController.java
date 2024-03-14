package com.nvd.bookstore.controller.admin;

import com.nvd.bookstore.entity.Author;
import com.nvd.bookstore.payload.request.AuthorRequest;
import com.nvd.bookstore.service.AuthorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/authors")
//@RolesAllowed("ADMIN")
public class AdminAuthorController {

    private final Logger logger = LoggerFactory.getLogger(AdminAuthorController.class);

    @Autowired
    private AuthorService authorService;

    @PostMapping
    public ResponseEntity<Author> addAuthor(@RequestBody AuthorRequest authorRequest) {
        logger.info("Add author" + authorRequest.getName());
        return ResponseEntity.ok(authorService.addAuthor(authorRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Author> updateAuthor(@PathVariable Long id, @RequestBody AuthorRequest authorRequest) {
        return ResponseEntity.ok(authorService.updateAuthor(id, authorRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAuthor(@PathVariable Long id) {
        authorService.deleteAuthor(id);
        return new ResponseEntity<>("Delete author successfully", HttpStatus.OK);
    }
}
