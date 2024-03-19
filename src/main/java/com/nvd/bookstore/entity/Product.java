package com.nvd.bookstore.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor
@Data
@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id", nullable = false)
    private Long id;
    @Column(columnDefinition = "text")
    private String description;

    private String image1;
    @Transient  // chỉ định nay không được ánh xạ vào cơ sở dữ liệu.
    private MultipartFile image_posted1;
    private String image2;
    @Transient
    private MultipartFile image_posted2;
    private String image3;
    @Transient
    private MultipartFile image_posted3;


    @JsonProperty("isActive")
    private boolean isActive;
    @JsonProperty("isDeleted")
    private boolean isDeleted;
    private String name;
    private Float price;
    private Float salePrice;
    private int stockQuantity;
    @Column(name = "year_of_publication", nullable = true)
    private int yearOfPublication;
    private int totalPages;
    private int soldQuantity;
    // Category
    // // dữ liệu liên quan (đối tượng mà mối quan hệ trỏ đến) sẽ không được lấy ngay lập tức khi truy xuất đối tượng hiện tại.
    // dữ liệu liên quan sẽ chỉ được nạp từ CSDL khi thực sự truy xuất đối tượng liên quan đó.
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", referencedColumnName = "category_id")
    @JsonIgnore
    private Category category;
    // Author
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "author_id", referencedColumnName = "author_id")
    @JsonIgnore
    private Author author;

    //------------ Mapped Column -----------//
    // Publisher
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "publisher_id", referencedColumnName = "publisher_id")
    @JsonIgnore
    private Publisher publisher;

    public String getImage1() {
        return "/static/images/products/1/" + image1;
    }

    public String getImage2() {
        return "/static/images/products/2/" + image1;
    }

    public String getImage3() {
        return "/static/images/products/3/" + image1;
    }
    //--------------------------------------//
}