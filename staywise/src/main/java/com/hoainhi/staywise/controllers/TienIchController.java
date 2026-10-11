package com.hoainhi.staywise.controllers;

import com.hoainhi.staywise.dtos.TienIchDTO;
import com.hoainhi.staywise.entities.TheLoaiTienIch;
import com.hoainhi.staywise.entities.TienIch;
import com.hoainhi.staywise.enums.TrangThai;
import com.hoainhi.staywise.services.serviceimpl.TienIchServiceImpl;
import jakarta.validation.Valid;
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
    public ResponseEntity<?> saveTienIch(@Valid @RequestBody TienIchDTO tienIchDTO){
        try {
            boolean themMoi = tienIchDTO.getId() == null;

            TienIch tienIch;
            if(themMoi){
                tienIch = new TienIch();
                tienIch.setTrangThai(TrangThai.DANG_HOAT_DONG);
            } else {
                tienIch = tienIchService.findById(tienIchDTO.getId()).orElseThrow(() -> new RuntimeException("khong tim thay"));

            }
            BeanUtils.copyProperties(
                    tienIchDTO,
                    tienIch,
                    "id",
                    "trangThai",
                    "theLoaiTienIch"
            );
            tienIchService.themTienIch(tienIch, tienIchDTO.getMatheloai());
            String message = themMoi
                    ? "Thêm tiện ích thành công!"
                    : "Sửa tiện ích thành công!";

            return ResponseEntity.ok(message);
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
