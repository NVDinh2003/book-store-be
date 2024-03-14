package com.nvd.bookstore.repository.address;

import com.nvd.bookstore.entity.AddressWard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WardRepository extends JpaRepository<AddressWard, String> {

    List<AddressWard> findByDistrictCode(String districtCode);
}
