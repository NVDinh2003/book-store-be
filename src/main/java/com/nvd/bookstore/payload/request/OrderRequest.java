package com.nvd.bookstore.payload.request;

import com.nvd.bookstore.entity.OrderDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {
    private Float grandTotal;
    private String orderStatus;
    private String paymentMethod;
    private String shippingAddress;
    private Float shippingTotal;
    @CreationTimestamp
    private Date orderDate;

    private List<OrderDetail> orderDetailList;
    private Long userId;
    private String voucherCode;

    public OrderRequest(Float grandTotal, String orderStatus, String paymentMethod, Float shippingTotal) {
        this.grandTotal = grandTotal;
        this.orderStatus = orderStatus;
        this.paymentMethod = paymentMethod;
        this.shippingTotal = shippingTotal;
    }
}