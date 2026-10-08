package com.hoainhi.staywise.controllers;

import com.hoainhi.staywise.dtos.DichVuDTO;
import com.hoainhi.staywise.entities.DichVu;
import com.hoainhi.staywise.enums.TrangThai;
import com.hoainhi.staywise.services.serviceimpl.DichVuServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/dichVu")
public class DichVuController {
    @Autowired
    private DichVuServiceImpl dichVuService;

    @GetMapping("")
    public ResponseEntity<?> showAll( @RequestParam(defaultValue = "") String tuKhoa,
                                      @RequestParam(defaultValue = "0") int trang,  @RequestParam(defaultValue = "5") int kichThuoc){
        Page<DichVu> dichVus = dichVuService.timKiem(tuKhoa, trang, kichThuoc);
        return ResponseEntity.ok(dichVus);
    }

    @PostMapping("/save")
    public ResponseEntity<?> themDichVu(@Valid @RequestBody DichVuDTO dichVuDTO){
        try{
            DichVu dichVu = new DichVu();
            BeanUtils.copyProperties(dichVuDTO, dichVu);
            dichVu.setTrangThai(TrangThai.DANG_HOAT_DONG);
            dichVuService.themDichVu(dichVu);
             String message = dichVuDTO.getId() == null ? "Them thanh cong!" : "Sua thanh cong";
            return ResponseEntity.ok(message);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }



    @DeleteMapping("/xoa/{id}")
    public ResponseEntity<?> xoaDichVu(@PathVariable Long id){
        try{
            dichVuService.xoa(id);
            return  ResponseEntity.ok().body("Xoa thanh cong");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
