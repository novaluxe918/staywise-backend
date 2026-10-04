package com.hoainhi.staywise.controllers;

import com.hoainhi.staywise.dtos.TheLoaiTienTichDTO;
import com.hoainhi.staywise.entities.TheLoaiTienIch;
import com.hoainhi.staywise.enums.TrangThai;
import com.hoainhi.staywise.services.serviceimpl.TheLoaiTienIchServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/theloaitienich")
public class TheLoaiTienIchController {
    @Autowired
    private TheLoaiTienIchServiceImpl theLoaiTienIchService;

    @GetMapping("/search")
    public ResponseEntity<?> searchTenTheLoai(  @RequestParam(defaultValue = "") String tuKhoa,
                                                @RequestParam(defaultValue = "0") int trang,
                                                @RequestParam(defaultValue = "5") int kichThuoc){
        Page<TheLoaiTienIch> tienIches = theLoaiTienIchService.timKiem(tuKhoa, trang, kichThuoc);
       return ResponseEntity.ok(tienIches);
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveTheLoai(@RequestBody TheLoaiTienTichDTO theLoaiTienTichDTO) {
        try {
            TheLoaiTienIch theLoaiTienIch = new TheLoaiTienIch();
            BeanUtils.copyProperties(theLoaiTienTichDTO, theLoaiTienIch);
            theLoaiTienIch.setTrangThai(TrangThai.DANG_HOAT_DONG);
            theLoaiTienIchService.themTheLoai(theLoaiTienIch);
            return ResponseEntity.ok("Them thanh cong!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Them that bai!");

        }
    }

    @DeleteMapping("/xoa/{id}")
    public ResponseEntity<?> xoaTheLoai(@PathVariable Long id){
        try{
            theLoaiTienIchService.xoa(id);
            return ResponseEntity.ok("Xoa thanh cong!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Xoa that bai!");
        }
    }



}
