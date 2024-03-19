package com.nvd.bookstore.payload.response;

import com.nvd.bookstore.entity.OrderDetail;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class OrderResponse {
    private Long id;
    private Date orderDate;
    private String orderStatus;
    private String paymentMethod;
    private String shippingAddress;
    private float shippingTotal;
    private float grandTotal;
    //    private float totalProductPrice;
    private Long userId;
    private List<OrderDetail> orderDetails;
    private String voucherCode;
    private float discountAmount;


    // Constructors, getters, and setters
}