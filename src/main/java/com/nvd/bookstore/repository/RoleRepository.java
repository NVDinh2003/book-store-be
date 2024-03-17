package com.nvd.bookstore.repository;

import com.nvd.bookstore.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {


    Role findByName(String name);


    @Query(value = "SELECT r.name "
            + "FROM user u "
            + "INNER JOIN user_role ur ON u.user_id = ur.user_id "
            + "INNER JOIN role r ON ur.role_id = r.role_id "
            + "WHERE u.email=:userEmail",
            countQuery = "select count(*) "
                    + "FROM user u "
                    + "INNER JOIN user_role ur ON u.user_id = ur.user_id "
                    + "INNER JOIN role r ON ur.role_id = r.role_id "
                    + "WHERE u.email=:userEmail",
            nativeQuery = true)
    List<Role> getRolesByUserEmail(String userEmail);
}
