package com.nvd.bookstore.service;

import com.nvd.bookstore.entity.Voucher;
import com.nvd.bookstore.exception.ResourceNotFoundException;
import com.nvd.bookstore.payload.request.VoucherRequest;
import com.nvd.bookstore.repository.VoucherRepository;
import com.nvd.bookstore.untils.AppConstant;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class VoucherService {

    @Autowired
    private VoucherRepository voucherRepository;

    @Autowired
    private ModelMapper modelMapper;


    public List<Voucher> getAllVouchers() {
        return voucherRepository.findAll();
    }

    public Voucher getVoucherById(Long id) {
        if (voucherRepository.findById(id).isPresent()) {
            return voucherRepository.findById(id).get();
        } else throw new ResourceNotFoundException(AppConstant.VOUCHER_NOT_FOUND + id);
    }

    public Voucher getVoucherByCode(String code) {
        Voucher voucher = voucherRepository.findByCode(code);
        if (voucher != null) {
            return voucher;
        } else throw new ResourceNotFoundException(AppConstant.VOUCHER_NOT_FOUND + code);
    }

    public List<Voucher> getActiveVouchers() {
        return voucherRepository.findByStatus(true);
    }

    public List<Voucher> getInactiveVouchers() {
        return voucherRepository.findByStatus(false);
    }

    public boolean checkVoucher(String code) {
        Voucher voucher = voucherRepository.findByCode(code);
        if (voucher != null) {
            return voucher.isStatus() && voucher.getExpiredDate().before(new Date());
        } else throw new ResourceNotFoundException(AppConstant.VOUCHER_NOT_FOUND + code);
    }

//    public Voucher searchVoucher(String code) {
//
//    }

    public Voucher createVoucher(VoucherRequest voucherRequest) {
        Voucher voucher = modelMapper.map(voucherRequest, Voucher.class);
        return voucherRepository.save(voucher);
    }

    public Voucher updateVoucher(Long id, VoucherRequest voucherRequest) {
        Optional<Voucher> voucher = voucherRepository.findById(id);

        if (voucher.isPresent()) {
            Voucher voucherUpdate = voucher.get();
            voucherUpdate = modelMapper.map(voucherRequest, Voucher.class);
            return voucherRepository.save(voucherUpdate);
        } else {
            throw new ResourceNotFoundException(AppConstant.VOUCHER_NOT_FOUND + id);
        }
    }

    public void deleteVoucher(Long id) {
        Optional<Voucher> voucher = voucherRepository.findById(id);
        if (voucher.isPresent()) {
            Voucher voucherUpdate = voucher.get();
            voucherUpdate.setStatus(false);
            voucherRepository.save(voucherUpdate);
        } else {
            throw new ResourceNotFoundException(AppConstant.VOUCHER_NOT_FOUND + id);
        }
    }
}
