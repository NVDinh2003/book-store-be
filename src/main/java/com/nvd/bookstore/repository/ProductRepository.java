package com.nvd.bookstore.repository;

import com.nvd.bookstore.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
//@CrossOrigin("http://localhost:4200")
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query(value = "select p.* " +
            "from product p " +
            "join category c on p.category_id = c.category_id " +
            "join author a on p.author_id = a.author_id " +
            "join publisher pu on p.publisher_id = pu.publisher_id " +
            "where p.name like %:keyWord% or p.description like %:keyWord% " +
            "or c.name like %:keyWord% " +
            "or a.name like %:keyWord% " +
            "or pu.name like %:keyWord%",
            countQuery = "select count(*) " +
                    "from product p " +
                    "join category c on p.category_id = c.category_id " +
                    "join author a on p.author_id = a.author_id " +
                    "join publisher pu on p.publisher_id = pu.publisher_id " +
                    "where p.name like %:keyWord% or p.description like %:keyWord% " +
                    "or c.name like %:keyWord% " +
                    "or a.name like %:keyWord% " +
                    "or pu.name like %:keyWord%",
            nativeQuery = true)
    Page<Product> search(@Param("keyWord") String keyWord, Pageable pageable);

    List<Product> findTop10ByOrderBySoldQuantityDesc();

    List<Product> findTop10ByOrderByYearOfPublicationDesc();

    List<Product> findAllByCategoryId(Long categoryId);

    List<Product> findAllByAuthorId(Long id);

    List<Product> findAllByPublisherId(Long id);
}
