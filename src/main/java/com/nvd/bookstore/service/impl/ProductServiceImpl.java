package com.nvd.bookstore.service.impl;

import com.nvd.bookstore.entity.Author;
import com.nvd.bookstore.entity.Category;
import com.nvd.bookstore.entity.Product;
import com.nvd.bookstore.entity.Publisher;
import com.nvd.bookstore.exception.ResourceNotFoundException;
import com.nvd.bookstore.payload.request.ProductRequest;
import com.nvd.bookstore.repository.AuthorRepository;
import com.nvd.bookstore.repository.CategoryRepository;
import com.nvd.bookstore.repository.ProductRepository;
import com.nvd.bookstore.repository.PublisherRepository;
import com.nvd.bookstore.service.ProductService;
import com.nvd.bookstore.untils.AppConstant;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private PublisherRepository publisherRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Page<Product> findAll(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    @Override
    public Product getProductById(Long productId) {
        if (productRepository.findById(productId).isPresent()) {
            return productRepository.findById(productId).get();
        } else throw new ResourceNotFoundException(AppConstant.PRODUCT_NOT_FOUND + productId);
    }

    @Override
    public Product addProduct(ProductRequest productRequest) {
        Category category = categoryRepository.findById(productRequest.getCategoryId())
                .orElseThrow(
                        () -> new ResourceNotFoundException(AppConstant.CATEGORY_NOT_FOUND + productRequest.getCategoryId()));

        Author author = authorRepository.findById(productRequest.getAuthorId())
                .orElseThrow(
                        () -> new ResourceNotFoundException(AppConstant.AUTHOR_NOT_FOUND + productRequest.getAuthorId()));

        Publisher publisher = publisherRepository.findById(productRequest.getPublisherId())
                .orElseThrow(
                        () -> new ResourceNotFoundException(AppConstant.PUBLISHER_NOT_FOUND + productRequest.getPublisherId()));

        Product product = modelMapper.map(productRequest, Product.class);

        product.setCategory(category);
        product.setAuthor(author);
        product.setPublisher(publisher);

        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(Long productId, ProductRequest productRequest) {
        Category category = categoryRepository.findById(productRequest.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException(AppConstant.CATEGORY_NOT_FOUND + productRequest.getCategoryId()));

        Author author = authorRepository.findById(productRequest.getAuthorId())
                .orElseThrow(() -> new ResourceNotFoundException(AppConstant.AUTHOR_NOT_FOUND + productRequest.getAuthorId()));

        Publisher publisher = publisherRepository.findById(productRequest.getPublisherId())
                .orElseThrow(() -> new ResourceNotFoundException(AppConstant.PUBLISHER_NOT_FOUND + productRequest.getPublisherId()));

        Optional<Product> product = productRepository.findById(productId);

        if (product.isPresent()) {
            Product productToUpdate = product.get();

            productToUpdate = modelMapper.map(productRequest, Product.class);

            productToUpdate.setCategory(category);
            productToUpdate.setAuthor(author);
            productToUpdate.setPublisher(publisher);

            return productRepository.save(productToUpdate);
        } else {
            throw new ResourceNotFoundException(AppConstant.PRODUCT_NOT_FOUND + productId);
        }

        // Cập nhật từng thuộc tính của sản phẩm dựa trên ProductRequest
//        productToUpdate.setDescription(productRequest.getDescription());
//        productToUpdate.setImage1(productRequest.getImage1());
//        productToUpdate.setImage2(productRequest.getImage2());
//        productToUpdate.setName(productRequest.getName());
//        productToUpdate.setPrice(productRequest.getPrice());
//        productToUpdate.setSalePrice(productRequest.getSalePrice());
//        productToUpdate.setStockQuantity(productRequest.getStockQuantity());
//        productToUpdate.setYearOfPublication(productRequest.getYearOfPublication());
//        productToUpdate.setTotalPages(productRequest.getTotalPages());
//        productToUpdate.setActive(productRequest.isActive());
//        productToUpdate.setDeleted(productRequest.isDeleted());
//        productToUpdate.setSoldQuantity(productRequest.getSoldQuantity());

    }

    @Override
    public void deleteProduct(Long productId) {
//        productRepository.deleteById(productId);
        Product productToDelete = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(AppConstant.PRODUCT_NOT_FOUND + productId));
        productToDelete.setDeleted(true);

        productRepository.save(productToDelete);
    }

    @Override
    public void setProductIsDeleted(Long productId) {

    }

    @Override
    public Page<Product> searchProduct(String keyWord, Pageable pageable) {
        return productRepository.search(keyWord, pageable);
    }

    @Override
    public List<Product> top10BestSeller() {
        return productRepository.findTop10ByOrderBySoldQuantityDesc();
    }

    @Override
    public List<Product> top10LatestBooks() {
        return productRepository.findTop10ByOrderByYearOfPublicationDesc();
    }

    @Override
    public ResponseEntity<String> uploadImages(MultipartFile image1, MultipartFile image2, MultipartFile image3) {
        try {
            if (!image1.isEmpty()) {
                String fileName1 = StringUtils.cleanPath(Objects.requireNonNull(image1.getOriginalFilename()));
                Path path1 = Paths.get("src/main/resources/static/images/products/1" + fileName1);
                Files.copy(image1.getInputStream(), path1, StandardCopyOption.REPLACE_EXISTING);
            }

            if (!image2.isEmpty()) {
                String fileName2 = StringUtils.cleanPath(Objects.requireNonNull(image2.getOriginalFilename()));
                Path path2 = Paths.get("src/main/resources/static/images/products/2" + fileName2);
                Files.copy(image2.getInputStream(), path2, StandardCopyOption.REPLACE_EXISTING);
            }

            if (!image3.isEmpty()) {
                String fileName3 = StringUtils.cleanPath(Objects.requireNonNull(image3.getOriginalFilename()));
                Path path3 = Paths.get("src/main/resources/static/images/products/3" + fileName3);
                Files.copy(image3.getInputStream(), path3, StandardCopyOption.REPLACE_EXISTING);
            }

            return ResponseEntity.ok().body("Images uploaded successfully");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload images.");
        }
    }
}
