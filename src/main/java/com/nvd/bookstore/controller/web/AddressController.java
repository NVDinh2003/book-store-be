package com.nvd.bookstore.controller.web;

import com.nvd.bookstore.entity.AddressDistrict;
import com.nvd.bookstore.entity.AddressProvince;
import com.nvd.bookstore.entity.AddressWard;
import com.nvd.bookstore.repository.address.DistrictRepository;
import com.nvd.bookstore.repository.address.ProvinceRepository;
import com.nvd.bookstore.repository.address.WardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/address")
public class AddressController {
    @Autowired
    private ProvinceRepository provinceRepository;

    @Autowired
    private DistrictRepository districtRepository;

    @Autowired
    private WardRepository wardRepository;


    @GetMapping("/provinces")
    public ResponseEntity<List<AddressProvince>> getAllProvinces() {
        return new ResponseEntity<>(provinceRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping("/districts")
    public ResponseEntity<List<AddressDistrict>> getAllDistricts() {
        return new ResponseEntity<>(districtRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping("/wards")
    public ResponseEntity<List<AddressWard>> getAllWards() {
        return new ResponseEntity<>(wardRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping("/districts/{provinceCode}")
    public ResponseEntity<List<AddressDistrict>> getDistrictByProvinceId(@PathVariable("provinceCode") String provinceCode) {
        return new ResponseEntity<>(districtRepository.findByProvinceCode(provinceCode), HttpStatus.OK);
    }

    @GetMapping("/wards/{districtCode}")
    public ResponseEntity<List<AddressWard>> getWardByDistrictId(@PathVariable("districtCode") String districtCode) {
        return new ResponseEntity<>(wardRepository.findByDistrictCode(districtCode), HttpStatus.OK);
    }
}
