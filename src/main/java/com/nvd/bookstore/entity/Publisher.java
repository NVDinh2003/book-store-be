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
@Table(name = "publisher")
public class Publisher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "publisher_id", nullable = false)
    private Long id;
    private String name;
    @JsonProperty("isDeleted")
    private boolean isDeleted;

    //------------ Mapped Column -----------//

    //--------------------------------------//

}