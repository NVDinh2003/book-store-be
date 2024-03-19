package com.nvd.bookstore.repository;

import com.nvd.bookstore.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    List<Cart> findByUserId(Long id);

    Optional<Cart> findByIdAndUserId(Long cartId, Long id);
}
