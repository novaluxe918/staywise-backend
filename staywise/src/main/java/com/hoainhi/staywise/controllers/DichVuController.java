package com.hoainhi.staywise.controllers;

import com.hoainhi.staywise.dtos.DichVuDTO;
import com.hoainhi.staywise.dtos.TheLoaiTienTichDTO;
import com.hoainhi.staywise.entities.DichVu;
import com.hoainhi.staywise.enums.TrangThai;
import com.hoainhi.staywise.services.serviceimpl.DichVuServiceImpl;
import com.hoainhi.staywise.validations.CapNhat;
import com.hoainhi.staywise.validations.ThemMoi;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import jakarta.validation.groups.Default;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/dichVu")
public class DichVuController {
    @Autowired
    private DichVuServiceImpl dichVuService;

    @Autowired
    private Validator validator;

    @GetMapping("")
    public ResponseEntity<?> showAll( @RequestParam(defaultValue = "") String tuKhoa,
                                      @RequestParam(defaultValue = "0") int trang,  @RequestParam(defaultValue = "5") int kichThuoc){
        Page<DichVu> dichVus = dichVuService.timKiem(tuKhoa, trang, kichThuoc);
        return ResponseEntity.ok(dichVus);
    }

    @PostMapping("/save")
    public ResponseEntity<?> themDichVu(@ModelAttribute DichVuDTO dichVuDTO){
        Class<?> group = dichVuDTO.getId() == null
                ? ThemMoi.class
                : CapNhat.class;
        Set<ConstraintViolation<DichVuDTO>> errors =
                validator.validate(dichVuDTO, Default.class, group);

        if (!errors.isEmpty()) {
            List<String> messages = errors.stream()
                    .map(ConstraintViolation::getMessage)
                    .toList();

            return ResponseEntity.badRequest().body(messages);
        }

        String message = dichVuService.saveDichVu(dichVuDTO);
        return ResponseEntity.ok(message);

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
