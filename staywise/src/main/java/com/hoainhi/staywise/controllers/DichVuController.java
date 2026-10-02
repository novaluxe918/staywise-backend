package com.hoainhi.staywise.controllers;

import com.hoainhi.staywise.dtos.DichVuDTO;
import com.hoainhi.staywise.entities.DichVu;
import com.hoainhi.staywise.enums.TrangThai;
import com.hoainhi.staywise.services.serviceimpl.DichVuServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/dichVu")
public class DichVuController {
    @Autowired
    private DichVuServiceImpl dichVuService;

    @PostMapping("/save")
    private ResponseEntity<?> themDichVu(@RequestBody DichVuDTO dichVuDTO){
        try{
            DichVu dichVu = new DichVu();
            BeanUtils.copyProperties(dichVuDTO, dichVu);
            dichVu.setTrangThai(TrangThai.DANG_HOAT_DONG);
            dichVuService.themDichVu(dichVu);
            return ResponseEntity.ok("Them thanh cong!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }
}
