package com.nvd.bookstore.repository;

import com.nvd.bookstore.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    //    User findByEmail(String email);
    Optional<User> findByEmail(String email);

//    @Query("SELECT u FROM User u JOIN FETCH u.roles WHERE u.email = :email")
//    Optional<User> findByEmailWithRoles(@Param("email") String email);

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);
}
