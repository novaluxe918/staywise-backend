package com.hoainhi.staywise.controllers;

import com.hoainhi.staywise.dtos.PhongDTO;
import com.hoainhi.staywise.entities.Phong;
import com.hoainhi.staywise.services.PhongService;
import com.hoainhi.staywise.services.serviceimpl.PhongServiceImpl;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/phong")
public class PhongController {
    @Autowired
    private PhongServiceImpl phongService;

    @GetMapping("/timKiem")
    public ResponseEntity<?> showAll( @RequestParam(defaultValue = "") String tuKhoa,
                                      @RequestParam(defaultValue = "0") int trang,  @RequestParam(defaultValue = "5") int kichThuoc){
        Page<PhongDTO> phongs = phongService.showAll(tuKhoa , trang, kichThuoc);
        return ResponseEntity.ok(phongs);
    }

    @PostMapping(value = "/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> themPhong( @ModelAttribute PhongDTO dto){
        try {
            String message = phongService.savePhong(dto);
            return ResponseEntity.ok(message);

        } catch (ConstraintViolationException e) {
            return ResponseEntity.badRequest().body(e.getMessage());

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/xoa/{id}")
    public ResponseEntity<?> xoaPhong(@PathVariable Long id){
        try{
            phongService.xoa(id);
            return ResponseEntity.ok().body("Xoa phong thanh cong");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }


    }
}
