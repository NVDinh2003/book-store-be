package com.nvd.bookstore.repository.address;

import com.nvd.bookstore.entity.AddressProvince;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProvinceRepository extends JpaRepository<AddressProvince, String> {

}
