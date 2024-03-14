package com.nvd.bookstore.repository;

import com.nvd.bookstore.entity.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, Long> {
    Voucher findByCode(String code);

    List<Voucher> findByStatus(boolean b);
}
