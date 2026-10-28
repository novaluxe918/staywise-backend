package com.hoainhi.staywise.controllers;


import com.hoainhi.staywise.dtos.ProvinceDTO;
import com.hoainhi.staywise.dtos.WardsDTO;
import com.hoainhi.staywise.services.serviceimpl.ProvinceServiceImpl;
import com.hoainhi.staywise.services.serviceimpl.WardServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/hanh-chinh")
public class DiaChiHanhChinhController {
    @Autowired
    private ProvinceServiceImpl provinceService;

    @Autowired
    private WardServiceImpl wardService;

    @GetMapping("/tinh-thanh")
    public List<ProvinceDTO> getAllProvinces() {
        return provinceService.getAllProvinces();
    }

    @GetMapping("/phuong-xa/{maTinh}")
    public List<WardsDTO> getWards(
            @PathVariable Integer maTinh) {
        return wardService.getWardsByProvince(maTinh);
    }
}
