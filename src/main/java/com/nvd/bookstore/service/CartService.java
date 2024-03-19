package com.nvd.bookstore.service;

import com.nvd.bookstore.entity.Cart;
import com.nvd.bookstore.entity.Product;
import com.nvd.bookstore.entity.User;
import com.nvd.bookstore.exception.ResourceNotFoundException;
import com.nvd.bookstore.repository.CartRepository;
import com.nvd.bookstore.repository.ProductRepository;
import com.nvd.bookstore.repository.UserRepository;
import com.nvd.bookstore.untils.AppConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public Cart addToCart(Cart cart, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(AppConstant.USER_ID_NOT_FOUND + userId));
        Product product = productRepository.findById(cart.getProduct().getId())
                .orElseThrow(() -> new ResourceNotFoundException(AppConstant.PRODUCT_NOT_FOUND + cart.getProduct().getId()));

        cart.setUser(user);
        cart.setProduct(product);
        return cartRepository.save(cart);
    }

    public List<Cart> getCartItems(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(AppConstant.USER_ID_NOT_FOUND + userId));
        return cartRepository.findByUserId(user.getId());
    }

    public Cart updateCartItem(Long userId, Long cartId, Cart cart) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(AppConstant.USER_ID_NOT_FOUND + userId));
        Cart existingCart = cartRepository.findByIdAndUserId(cartId, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException(AppConstant.CART_ITEM_NOT_FOUND + cartId));

        existingCart.setQuantity(cart.getQuantity());
        return cartRepository.save(existingCart);
    }

    public void removeCartItem(Long userId, Long cartId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(AppConstant.USER_ID_NOT_FOUND + userId));
        Cart cart = cartRepository.findByIdAndUserId(cartId, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException(AppConstant.CART_ITEM_NOT_FOUND + cartId));
        cartRepository.delete(cart);
    }

    public void removeAllCartItems(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(AppConstant.USER_ID_NOT_FOUND + userId));
        List<Cart> cartItems = cartRepository.findByUserId(user.getId());
        cartRepository.deleteAll(cartItems);
    }
}
