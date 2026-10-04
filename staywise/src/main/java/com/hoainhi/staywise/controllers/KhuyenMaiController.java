package com.hoainhi.staywise.controllers;

import com.hoainhi.staywise.dtos.KhuyenMaiDTO;
import com.hoainhi.staywise.entities.KhuyenMai;
import com.hoainhi.staywise.enums.TrangThaiKhuyenMai;
import com.hoainhi.staywise.services.serviceimpl.KhuyenMaiServiceimpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/khuyenMai")
public class KhuyenMaiController {
    @Autowired
    private KhuyenMaiServiceimpl khuyenMaiServiceimpl;

    @PostMapping("/save")
    public ResponseEntity<?> themKhuyenMai(@RequestBody KhuyenMaiDTO khuyenMaiDTO){
        try {
            KhuyenMai khuyenMai = new KhuyenMai();
            BeanUtils.copyProperties(khuyenMaiDTO, khuyenMai);
            khuyenMai.setTrangThaiKhuyenMai(TrangThaiKhuyenMai.DANG_HOAT_DONG);
            khuyenMaiServiceimpl.themKhuyenMai(khuyenMai);
            return ResponseEntity.ok().body("Them thanh cong!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}
