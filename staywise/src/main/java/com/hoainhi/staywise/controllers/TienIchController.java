package com.hoainhi.staywise.controllers;

import com.hoainhi.staywise.dtos.TienIchDTO;
import com.hoainhi.staywise.entities.TheLoaiTienIch;
import com.hoainhi.staywise.entities.TienIch;
import com.hoainhi.staywise.enums.TrangThai;
import com.hoainhi.staywise.services.serviceimpl.TienIchServiceImpl;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/tienich")
public class TienIchController {
    @Autowired
    private TienIchServiceImpl tienIchService;

    @GetMapping("/timKiem")
    public ResponseEntity<?> timKiemTienIch(@RequestParam(defaultValue = "") String tuKhoa,
                                            @RequestParam(defaultValue = "0") int trang,
                                            @RequestParam(defaultValue = "5") int kichThuoc){
        Page<TienIch> tienIches = tienIchService.timKiemTienIch(tuKhoa, trang, kichThuoc);
        return ResponseEntity.ok(tienIches);
    }

    @PostMapping(value = "/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> saveTienIch( @ModelAttribute TienIchDTO tienIchDTO){
       try{
           String message = tienIchService.saveTienIch(tienIchDTO);
           return ResponseEntity.ok(message);
       } catch (ConstraintViolationException e) {
           return ResponseEntity.badRequest().body(e.getMessage());

       } catch (RuntimeException e) {
           return ResponseEntity.badRequest().body(e.getMessage());
       }
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
