package com.nvd.bookstore.controller.admin;


import com.nvd.bookstore.entity.Voucher;
import com.nvd.bookstore.payload.request.VoucherRequest;
import com.nvd.bookstore.service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/vouchers")
public class AdminVoucherController {

    @Autowired
    private VoucherService voucherService;

    @PostMapping
    public ResponseEntity<Voucher> createVoucher(@RequestBody VoucherRequest voucherRequest) {
        Voucher createdVoucher = voucherService.createVoucher(voucherRequest);
        return new ResponseEntity<>(createdVoucher, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Voucher> updateVoucher(@PathVariable("id") Long id, @RequestBody VoucherRequest voucherRequest) {
        Voucher updatedVoucher = voucherService.updateVoucher(id, voucherRequest);
        return new ResponseEntity<>(updatedVoucher, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteVoucher(@PathVariable("id") Long id) {
        voucherService.deleteVoucher(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
