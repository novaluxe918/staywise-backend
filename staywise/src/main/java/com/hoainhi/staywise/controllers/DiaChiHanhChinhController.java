package com.hoainhi.staywise.controllers;

import com.hoainhi.staywise.services.serviceimpl.DiaChiHanhChinhService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class DiaChiHanhChinhController {
    @Autowired
    private DiaChiHanhChinhService diaChiHanhChinhService;

    @GetMapping("/tinh-thanh")
    public String layTinhThanh() {
        return diaChiHanhChinhService.layDanhSachTinhThanh();
    }

    @GetMapping("/tinh-thanh/{maTinh}/phuong-xa")
    public String layPhuongXa(@PathVariable Integer maTinh) {
        return diaChiHanhChinhService.layPhuongXaTheoTinh(maTinh);
    }
}
