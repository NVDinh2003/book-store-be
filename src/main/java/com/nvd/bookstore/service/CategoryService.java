package com.nvd.bookstore.service;

import com.nvd.bookstore.entity.Category;
import com.nvd.bookstore.entity.Product;
import com.nvd.bookstore.exception.ResourceNotFoundException;
import com.nvd.bookstore.payload.request.CategoryRequest;
import com.nvd.bookstore.repository.CategoryRepository;
import com.nvd.bookstore.repository.ProductRepository;
import com.nvd.bookstore.untils.AppConstant;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ModelMapper modelMapper;


    public List<Category> getAll() {
        return categoryRepository.findAllByOrderByNameAsc();
    }

    public Category getById(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException(AppConstant.CATEGORY_NOT_FOUND + categoryId));
    }

    public List<Product> getProductsByCategory(Long categoryId) {
        return productRepository.findAllByCategoryId(categoryId);
    }

    public Category addCategory(CategoryRequest categoryRequest) {
        Category category = modelMapper.map(categoryRequest, Category.class);
        return categoryRepository.save(category);
    }

    public Category updateCategory(Long categoryId, CategoryRequest categoryRequest) {
        Category oldCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException(AppConstant.CATEGORY_NOT_FOUND + categoryId));

        oldCategory.setName(categoryRequest.getName());
        oldCategory.setActive(categoryRequest.isActive());
        oldCategory.setDeleted(categoryRequest.isDeleted());

        return categoryRepository.save(oldCategory);
    }

    public void deleteCategory(Long id) {
        Category categoryToDelete = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(AppConstant.CATEGORY_NOT_FOUND + id));
        categoryToDelete.setDeleted(true);

        categoryRepository.save(categoryToDelete);
    }
}
