package com.nvd.bookstore.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "wards")
public class AddressWard {
    @Id
    private String code;
    private String name;
    private String fullName;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "district_code", referencedColumnName = "code")
    @JsonIgnore
    private AddressDistrict district;
}
