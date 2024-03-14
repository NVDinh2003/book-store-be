package com.nvd.bookstore.repository.address;

import com.nvd.bookstore.entity.AddressDistrict;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DistrictRepository extends JpaRepository<AddressDistrict, String> {

    List<AddressDistrict> findByProvinceCode(String provinceCode);
}
