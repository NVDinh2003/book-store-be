package com.nvd.bookstore.service;

import com.nvd.bookstore.entity.Author;
import com.nvd.bookstore.entity.Product;
import com.nvd.bookstore.exception.ResourceNotFoundException;
import com.nvd.bookstore.payload.request.AuthorRequest;
import com.nvd.bookstore.repository.AuthorRepository;
import com.nvd.bookstore.repository.ProductRepository;
import com.nvd.bookstore.untils.AppConstant;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorService {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }

    public Author getAuthorById(Long id) {
        if (authorRepository.findById(id).isPresent()) {
            return authorRepository.findById(id).get();
        } else throw new ResourceNotFoundException(AppConstant.AUTHOR_NOT_FOUND + id);
    }

    public List<Product> getBooksByAuthorId(Long id) {
        if (authorRepository.findById(id).isPresent()) {
            return productRepository.findAllByAuthorId(id);
        } else throw new ResourceNotFoundException(AppConstant.AUTHOR_NOT_FOUND + id);
    }

    public Author addAuthor(AuthorRequest authorRequest) {
        Author author = modelMapper.map(authorRequest, Author.class);
        return authorRepository.save(author);
    }

    public void deleteAuthor(Long id) {
        Author authorToDelete = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(AppConstant.AUTHOR_NOT_FOUND + id));
        authorToDelete.setDeleted(false);
        authorRepository.save(authorToDelete);
    }

    public Author updateAuthor(Long id, AuthorRequest authorRequest) {
        Author oldAuthor = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(AppConstant.AUTHOR_NOT_FOUND + id));

        oldAuthor.setName(authorRequest.getName());
        oldAuthor.setDescription(authorRequest.getDescription());
        oldAuthor.setDeleted(authorRequest.isDeleted());

        return authorRepository.save(oldAuthor);
    }
}
