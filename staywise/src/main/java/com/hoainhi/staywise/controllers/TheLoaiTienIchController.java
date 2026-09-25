package com.hoainhi.staywise.controllers;

import com.hoainhi.staywise.dtos.TheLoaiTienTichDTO;
import com.hoainhi.staywise.entities.TheLoaiTienIch;
import com.hoainhi.staywise.services.serviceimpl.TheLoaiTienIchServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class TheLoaiTienIchController {
    @Autowired
    private TheLoaiTienIchServiceImpl theLoaiTienIchService;

    @PostMapping("/save")
    public ResponseEntity<?> saveTheLoai(@RequestBody TheLoaiTienTichDTO theLoaiTienTichDTO) {
        try {
            TheLoaiTienIch theLoaiTienIch = new TheLoaiTienIch();
            BeanUtils.copyProperties(theLoaiTienTichDTO, theLoaiTienIch);
            theLoaiTienIchService.themTheLoai(theLoaiTienIch);
            return ResponseEntity.ok("Them thanh cong!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Them that bai!");

        }
    }
}
