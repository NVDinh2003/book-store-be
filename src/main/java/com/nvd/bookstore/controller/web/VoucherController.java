package com.nvd.bookstore.controller.web;


import com.nvd.bookstore.entity.Voucher;
import com.nvd.bookstore.service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/vouchers")
public class VoucherController {

    @Autowired
    private VoucherService voucherService;

    @GetMapping
    public ResponseEntity<List<Voucher>> getAllVouchers() {
        List<Voucher> vouchers = voucherService.getAllVouchers();
        return new ResponseEntity<>(vouchers, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Voucher> getVoucherById(@PathVariable("id") Long id) {
        Voucher voucher = voucherService.getVoucherById(id);
        return new ResponseEntity<>(voucher, HttpStatus.OK);
    }

    @GetMapping("/{code}")
    public ResponseEntity<Voucher> getVoucherByCode(@PathVariable("code") String code) {
        return ResponseEntity.ok(voucherService.getVoucherByCode(code));
    }


    @GetMapping("/active")
    public ResponseEntity<List<Voucher>> getActiveVouchers() {
        return ResponseEntity.ok(voucherService.getActiveVouchers());
    }

    @GetMapping("/inactive")
    public ResponseEntity<List<Voucher>> getInactiveVouchers() {
        return ResponseEntity.ok(voucherService.getInactiveVouchers());
    }

    @GetMapping("/check/{code}")
    public ResponseEntity<Boolean> checkVoucher(@PathVariable("code") String code) {
        return ResponseEntity.ok(voucherService.checkVoucherByCode(code));
    }

}
