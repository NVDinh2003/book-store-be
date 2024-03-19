package com.nvd.bookstore.service;

import com.nvd.bookstore.entity.*;
import com.nvd.bookstore.exception.ResourceNotFoundException;
import com.nvd.bookstore.payload.request.OrderRequest;
import com.nvd.bookstore.payload.response.OrderResponse;
import com.nvd.bookstore.repository.CartRepository;
import com.nvd.bookstore.repository.OrderRepository;
import com.nvd.bookstore.repository.UserRepository;
import com.nvd.bookstore.repository.VoucherRepository;
import com.nvd.bookstore.untils.AppConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VoucherRepository voucherRepository;

    @Autowired
    private VoucherService voucherService;

    @Autowired
    private CartRepository cartRepository;

    public Page<Order> getAllOrders(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }

    public Order getOrderById(Long orderId) {
        Optional<Order> order = orderRepository.findById(orderId);
        if (order.isPresent())
            return order.get();
        else throw new ResourceNotFoundException(AppConstant.ORDER_NOT_FOUND + order);
    }

    public List<Order> getOrdersByUserEmail(String userEmail) {
        List<Order> orders = orderRepository.findByUserEmail(userEmail);
        if (orders.isEmpty())
            throw new ResourceNotFoundException(AppConstant.ORDER_NOT_FOUND + userEmail);
        else return orders;
    }


    public OrderResponse createOrder(String userEmail, OrderRequest orderRequest) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + userEmail));

        List<Cart> cartItems = cartRepository.findByUserId(user.getId());

//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
//        List<Cart> cartItems = cartRepository.findByUserId(userId);

        // Tạo đơn hàng mới
        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(new Date());
        order.setOrderStatus("Pending");
        order.setPaymentMethod(orderRequest.getPaymentMethod());
        order.setShippingAddress(user.getAddress());
//        order.setShippingTotal(calculateShippingCost(orderRequest.getShippingMethod()));
        order.setShippingTotal(0f);  // free ship

        // check và áp dụng Voucher
        Voucher voucher = null;
        float discountAmount = 0;
        if (orderRequest.getVoucherCode() != null && !orderRequest.getVoucherCode().isEmpty()) {
            voucher = voucherRepository.findByCode(orderRequest.getVoucherCode())
                    .orElseThrow(() -> new ResourceNotFoundException(AppConstant.VOUCHER_NOT_FOUND + orderRequest.getVoucherCode()));
            if (voucherService.checkVoucher(voucher))
                throw new ResourceNotFoundException(AppConstant.VOUCHER_NOT_VALID + voucher.getCode());
            discountAmount = calculateDiscountAmount(cartItems, voucher.getDiscountPercent());
            order.setVoucher(voucher);
        }

        // Tính tổng giá trị đơn hàng
        float totalProductPrice = 0;
        float grandTotal = 0;
        List<OrderDetail> orderDetails = new ArrayList<>();
        for (Cart cartItem : cartItems) {
            Product product = cartItem.getProduct();
            float productPrice = (product.getSalePrice() != null && product.getSalePrice() > 0) ? product.getSalePrice() : product.getPrice();
            float itemTotal = productPrice * cartItem.getQuantity();
            totalProductPrice += itemTotal;

            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setProduct(product);
            orderDetail.setQuantity(cartItem.getQuantity());
            orderDetail.setTotalPrice(itemTotal);
            orderDetail.setOrder(order);
            orderDetails.add(orderDetail);
        }
        grandTotal = totalProductPrice - discountAmount;
        order.setGrandTotal(grandTotal);
//        order.setTotalProductPrice(totalProductPrice);
        order.setOrderDetailList(orderDetails);

        Order savedOrder = orderRepository.save(order);

        cartRepository.deleteAll(cartItems);

        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setId(savedOrder.getId());
        orderResponse.setOrderDate(savedOrder.getOrderDate());
        orderResponse.setOrderStatus(savedOrder.getOrderStatus());
        orderResponse.setPaymentMethod(savedOrder.getPaymentMethod());
        orderResponse.setShippingAddress(user.getAddress());
        orderResponse.setShippingTotal(savedOrder.getShippingTotal());
        orderResponse.setGrandTotal(savedOrder.getGrandTotal());
//        orderResponse.setTotalProductPrice(savedOrder.getTotalProductPrice());
        orderResponse.setUserId(user.getId());
        orderResponse.setOrderDetails(savedOrder.getOrderDetailList());
        orderResponse.setVoucherCode(orderRequest.getVoucherCode());
        orderResponse.setDiscountAmount(discountAmount);

        return orderResponse;
    }

    public OrderResponse updateOrder(Long id, OrderRequest orderRequest) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(AppConstant.ORDER_NOT_FOUND + id));

        // update infor
        order.setOrderStatus(orderRequest.getOrderStatus());
        order.setPaymentMethod(orderRequest.getPaymentMethod());
        order.setPaymentMethod(orderRequest.getShippingAddress());
        order.setShippingTotal(orderRequest.getShippingTotal());

        // update  voucher infor
        Voucher voucher = null;
        float discountAmount = 0;
        if (orderRequest.getVoucherCode() != null && !orderRequest.getVoucherCode().isEmpty()
                && voucherService.checkVoucherByCode(orderRequest.getVoucherCode())) {
            voucher = voucherRepository.findByCode(orderRequest.getVoucherCode())
                    .orElseThrow(() -> new ResourceNotFoundException(AppConstant.VOUCHER_NOT_FOUND + orderRequest.getVoucherCode()));
            discountAmount = calculateDiscountAmount(order.getOrderDetailList(), voucher.getDiscountPercent());
        } else {
            discountAmount = calculateDiscountAmount(order.getOrderDetailList(), 0);
        }

        // update price order
        float totalProductPrice = order.getGrandTotal();
        float grandTotal = totalProductPrice - discountAmount;
        order.setGrandTotal(grandTotal);
        order.setVoucher(voucher);

        Order updatedOrder = orderRepository.save(order);

        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setId(updatedOrder.getId());
        orderResponse.setOrderDate(updatedOrder.getOrderDate());
        orderResponse.setOrderStatus(updatedOrder.getOrderStatus());
        orderResponse.setPaymentMethod(updatedOrder.getPaymentMethod());
        orderResponse.setShippingTotal(updatedOrder.getShippingTotal());
        orderResponse.setGrandTotal(updatedOrder.getGrandTotal());
//        orderResponse.setTotalProductPrice(updatedOrder.getTotalProductPrice());
        orderResponse.setUserId(updatedOrder.getUser().getId());
        orderResponse.setOrderDetails(updatedOrder.getOrderDetailList());
        orderResponse.setVoucherCode(orderRequest.getVoucherCode());
        orderResponse.setDiscountAmount(discountAmount);

        return orderResponse;
    }

    private <T> float calculateDiscountAmount(List<T> items, int discountPercent) {
        float totalProductPrice = 0;
        for (T item : items) {
            float productPrice;
            int quantity;

            if (item instanceof Cart cartItem) {
                Product product = cartItem.getProduct();
                productPrice = (product.getSalePrice() != null && product.getSalePrice() > 0) ? product.getSalePrice() : product.getPrice();
                quantity = cartItem.getQuantity();
            } else if (item instanceof OrderDetail orderDetail) {
                Product product = orderDetail.getProduct();
                productPrice = (product.getSalePrice() != null && product.getSalePrice() > 0) ? product.getSalePrice() : product.getPrice();
                quantity = orderDetail.getQuantity();
            } else {
                throw new IllegalArgumentException("Invalid item type: " + item.getClass().getName());
            }

            totalProductPrice += productPrice * quantity;
        }
        return discountPercent != 0 ? totalProductPrice * (discountPercent / 100.0f) : totalProductPrice;
    }

    public void deleteOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(AppConstant.ORDER_NOT_FOUND + id));

        orderRepository.delete(order);
    }

    //    private float calculateDiscountAmount(List<Cart> cartItems, int discountPercent) {
//        float totalProductPrice = 0;
//        for (Cart cartItem : cartItems) {
//            Product product = cartItem.getProduct();
//            float productPrice = (product.getSalePrice() != null && product.getSalePrice() > 0) ? product.getSalePrice() : product.getPrice();
//            totalProductPrice += productPrice * cartItem.getQuantity();
//        }
//        return totalProductPrice * (discountPercent / 100.0f);
//    }

//    private float calculateShippingCost(String shippingMethod) {
//        // Implement your logic to calculate shipping cost based on the shipping method
//        // For example:
//        if (shippingMethod.equals("Standard")) {
//            return 5.99f;
//        } else if (shippingMethod.equals("Express")) {
//            return 10.99f;
//        }
//        // Add more cases for other shipping methods
//        return 0f;
//    }
}
