package com.nvd.bookstore.controller.admin;

import com.nvd.bookstore.entity.User;
import com.nvd.bookstore.payload.request.UserRequest;
import com.nvd.bookstore.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/users")
public class AdminUserController {

    private final Logger logger = LoggerFactory.getLogger(AdminUserController.class);

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<Page<User>> getAllUsers(@RequestParam(defaultValue = "0") int pageNumber,
                                                  @RequestParam(defaultValue = "10") int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<User> userList = userService.getAllUsers(pageable);
        logger.info("Get all users" + userList.getContent().get(0).getEmail());
//        logger.info("Get all users" + userList.getContent().get(0).getRoles());
        return new ResponseEntity<>(userList, HttpStatus.OK);
    }

    @GetMapping("/{user_id}")
    public ResponseEntity<User> getUserById(
            @PathVariable("user_id") Long userId) {
        return new ResponseEntity<>(userService.getUserById(userId), HttpStatus.OK);
    }

    @PutMapping("{user_id}")
    public ResponseEntity<User> updateUser(
            @PathVariable("user_id") Long userId,
            @RequestBody UserRequest user) {
        return new ResponseEntity<>(userService.updateUser(userId, user), HttpStatus.OK);
    }

//    @PutMapping("{user_id}")
//    public ResponseEntity<User> updateUserStatus(
//            @PathVariable("user_id") Long userId,
//            @RequestBody UserStatusRequest user) {
//        return new ResponseEntity<>(userService.updateUserStatus(userId, user), HttpStatus.OK);
//    }

    @DeleteMapping("{user_id}")
    public ResponseEntity<String> deleteUser(
            @PathVariable("user_id") Long userId) {
        userService.deleteUser(userId);
        return new ResponseEntity<>("Delete user successfully", HttpStatus.OK);
    }

}
