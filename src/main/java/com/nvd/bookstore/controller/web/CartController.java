package com.nvd.bookstore.controller.web;

import com.nvd.bookstore.entity.Cart;
import com.nvd.bookstore.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users/{userId}/carts")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping
    public ResponseEntity<Cart> addToCart(@RequestBody Cart cart, @PathVariable Long userId) {
        Cart addedCart = cartService.addToCart(cart, userId);
        return ResponseEntity.ok(addedCart);
    }

    @GetMapping
    public ResponseEntity<List<Cart>> getCartItems(@PathVariable Long userId) {
        List<Cart> cartItems = cartService.getCartItems(userId);
        return ResponseEntity.ok(cartItems);
    }

    @PutMapping("/{cartId}")
    public ResponseEntity<Cart> updateCartItem(@PathVariable Long userId, @PathVariable Long cartId, @RequestBody Cart cart) {
        Cart updatedCart = cartService.updateCartItem(userId, cartId, cart);
        return ResponseEntity.ok(updatedCart);
    }

    @DeleteMapping("/{cartId}")
    public ResponseEntity<Void> removeCartItem(@PathVariable Long userId, @PathVariable Long cartId) {
        cartService.removeCartItem(userId, cartId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> removeAllCartItems(@PathVariable Long userId) {
        cartService.removeAllCartItems(userId);
        return ResponseEntity.noContent().build();
    }
}