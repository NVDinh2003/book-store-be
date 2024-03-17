package com.nvd.bookstore.service;

import com.nvd.bookstore.entity.User;
import com.nvd.bookstore.exception.ResourceNotFoundException;
import com.nvd.bookstore.payload.request.UserRequest;
import com.nvd.bookstore.payload.request.UserStatusRequest;
import com.nvd.bookstore.repository.UserRepository;
import com.nvd.bookstore.untils.AppConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> userEntity = userRepository.findByEmail(email);
        if (userEntity.isEmpty())
            throw new ResourceNotFoundException(AppConstant.USER_NOT_FOUND + email);
        return userEntity.get();
    }

    public Page<User> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public User getUserById(Long userId) {

        if (userRepository.findById(userId).isPresent()) {
            return userRepository.findById(userId).get();
        } else throw new ResourceNotFoundException(AppConstant.USER_NOT_FOUND + userId);
    }

    public User updateUser(Long userId, UserRequest updatedUser) {
        Optional<User> user = userRepository.findById(userId);

        if (user.isPresent()) {
            User existingUser = user.get();

            existingUser.setAddress(updatedUser.getAddress());
            existingUser.setAvatar(updatedUser.getAvatar());
            existingUser.setEmail(updatedUser.getEmail());
            existingUser.setFullName(updatedUser.getFullName());
            existingUser.setPassword(updatedUser.getPassword());
            existingUser.setPhone(updatedUser.getPhone());
            existingUser.setStatus(updatedUser.getStatus());
            existingUser.setRoles(updatedUser.getRoles());

            return userRepository.save(existingUser);
        } else {
            throw new ResourceNotFoundException(AppConstant.USER_NOT_FOUND + userId);
        }
    }

    public void deleteUser(Long userId) {
        User userToDelete = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(AppConstant.USER_NOT_FOUND + userId));
        userToDelete.setStatus(false);

        userRepository.save(userToDelete);
    }

    public User updateUserStatus(Long userId, UserStatusRequest userUpdateStatus) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            User existingUser = user.get();
            existingUser.setStatus(userUpdateStatus.getStatus());
            return userRepository.save(existingUser);
        } else {
            throw new ResourceNotFoundException(AppConstant.USER_NOT_FOUND + userId);
        }
    }


//    private Collection<GrantedAuthority> mapRolesToAuthorities(List<Role> roles) {
//        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
//    }
}
