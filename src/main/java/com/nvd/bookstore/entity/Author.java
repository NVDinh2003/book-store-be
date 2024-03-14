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
@Table(name = "author")
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "author_id", nullable = false)
    private Long id;
    private String name;
    @Column(columnDefinition = "text")
    private String description;
    @JsonProperty("isDeleted")
    private boolean isDeleted;

    public Author(String name) {
        this.name = name;
        this.isDeleted = false;
    }

    //------------ Mapped Column -----------//

    //--------------------------------------//
}