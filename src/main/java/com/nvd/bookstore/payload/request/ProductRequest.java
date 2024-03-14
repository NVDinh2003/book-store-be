package com.nvd.bookstore.payload.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ProductRequest {

    //    private Long id;
    private String description;

    private String image1;
    //    private MultipartFile image_posted1;
    private String image2;
//    private MultipartFile image_posted2;

    private String name;
    private Float price;
    private Float salePrice;
    private int stockQuantity;
    private int yearOfPublication;
    private int totalPages;
    private int soldQuantity;

    private Long categoryId;
    private Long authorId;
    private Long publisherId;
    @JsonProperty("isActive")
    private boolean isActive;
    @JsonProperty("isDeleted")
    private boolean isDeleted;

    public ProductRequest(String description, String image1, String image2, String name, Float price, Float salePrice, int stockQuantity, int yearOfPublication, int totalPages, Long categoryId, Long authorId, Long publisherId) {
        this.description = description;
        this.image1 = image1;
        this.image2 = image2;
        this.name = name;
        this.price = price;
        this.salePrice = salePrice;
        this.stockQuantity = stockQuantity;
        this.yearOfPublication = yearOfPublication;
        this.totalPages = totalPages;
        this.soldQuantity = 0;
        this.categoryId = categoryId;
        this.authorId = authorId;
        this.publisherId = publisherId;
        this.isActive = true;
        this.isDeleted = false;
    }
}
