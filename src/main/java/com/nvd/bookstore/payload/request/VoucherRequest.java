package com.nvd.bookstore.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VoucherRequest {

    private String code;
    private int discountPercent;
    private Date expiredDate;
    private int limitUsage;
    private String name;
    private boolean status;

    @CreationTimestamp
    private Date createdAt;
    @UpdateTimestamp
    private Date updatedAt;

    public VoucherRequest(String code, int discountPercent, Date expiredDate, int limitUsage, String name) {
        this.code = code;
        this.discountPercent = discountPercent;
        this.expiredDate = expiredDate;
        this.limitUsage = limitUsage;
        this.name = name;
        this.status = true;
    }
}
