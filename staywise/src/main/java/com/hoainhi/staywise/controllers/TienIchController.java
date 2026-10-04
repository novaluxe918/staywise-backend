package com.hoainhi.staywise.controllers;

import com.hoainhi.staywise.dtos.TienIchDTO;
import com.hoainhi.staywise.entities.TheLoaiTienIch;
import com.hoainhi.staywise.entities.TienIch;
import com.hoainhi.staywise.enums.TrangThai;
import com.hoainhi.staywise.services.serviceimpl.TienIchServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/tienich")
public class TienIchController {
    @Autowired
    private TienIchServiceImpl tienIchService;

    @PostMapping("/save")
    public ResponseEntity<?> saveTienIch(@RequestBody TienIchDTO tienIchDTO){
        try {
            TienIch tienIch = new TienIch();
            BeanUtils.copyProperties(tienIchDTO, tienIch);
            tienIch.setTrangThai(TrangThai.DANG_HOAT_DONG);
            tienIchService.themTienIch(tienIch, tienIchDTO.getMatheloai());
            return ResponseEntity.ok("them thanh cong!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Them that bai!");
        }
    }

    @GetMapping("/timKiem")
    public ResponseEntity<?> timKiemTienIch(@RequestParam(defaultValue = "") String tuKhoa,
                                             @RequestParam(defaultValue = "0") int trang,
                                             @RequestParam(defaultValue = "5") int kichThuoc){
        Page<TienIch> tienIches = tienIchService.timKiemTienIch(tuKhoa, trang, kichThuoc);
        return ResponseEntity.ok(tienIches);
    }

    @DeleteMapping("/xoa")
    public ResponseEntity<?> xoaTienIch(@PathVariable Long id){
        try{
            tienIchService.xoa(id);
            return ResponseEntity.ok("Xoa thanh cong!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Xoa that bai!");
        }
    }

}
