package com.hoainhi.staywise.controllers;

import com.hoainhi.staywise.dtos.LoaiGiuongDTO;
import com.hoainhi.staywise.entities.LoaiGiuong;
import com.hoainhi.staywise.enums.TrangThai;
import com.hoainhi.staywise.services.serviceimpl.LoaiGiuongServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/giuong")
public class GiuongController {

    @Autowired
    private LoaiGiuongServiceImpl loaiGiuongService;

    @GetMapping("/timkiem")
    public ResponseEntity<?> timKiem( @RequestParam(defaultValue = "") String tuKhoa,
                                      @RequestParam(defaultValue = "0") int trang,  @RequestParam(defaultValue = "5") int kichThuoc){
        Page<LoaiGiuong> loaiGiuongs = loaiGiuongService.timKiem(tuKhoa, trang, kichThuoc);
        return ResponseEntity.ok(loaiGiuongs);
    }

    @PostMapping("/save")
    public ResponseEntity<?> themLoaiGiuong(@Valid  @RequestBody  LoaiGiuongDTO loaiGiuongDTO){
        try{
            LoaiGiuong loaiGiuong = new LoaiGiuong();
            BeanUtils.copyProperties(loaiGiuongDTO, loaiGiuong);
            loaiGiuong.setTrangThai(TrangThai.DANG_HOAT_DONG);
            loaiGiuongService.themLoaiGiuong(loaiGiuong);
            return ResponseEntity.ok().body("Them thanh cong!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }



    @DeleteMapping("/xoa")
    public ResponseEntity<?> xoaLoaiGiuong(@PathVariable Long id){
        try{
            loaiGiuongService.xoa(id);
            return ResponseEntity.ok().body("Xoa thanh cong");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}
