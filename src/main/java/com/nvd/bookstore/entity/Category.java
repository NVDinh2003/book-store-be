package com.nvd.bookstore.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id", nullable = false)
    private Long id;
    private String name;
    @JsonProperty("isActive")
    private boolean isActive;
    @JsonProperty("isDeleted")
    private boolean isDeleted;

    public Category(String name) {
        this.name = name;
        this.isActive = true;
        this.isDeleted = false;
    }

    //------------ Mapped Column -----------//

    //--------------------------------------//
}