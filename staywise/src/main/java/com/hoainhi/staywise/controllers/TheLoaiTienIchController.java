package com.hoainhi.staywise.controllers;

import com.hoainhi.staywise.dtos.TheLoaiTienTichDTO;
import com.hoainhi.staywise.entities.TheLoaiTienIch;
import com.hoainhi.staywise.enums.TrangThai;
import com.hoainhi.staywise.services.serviceimpl.TheLoaiTienIchServiceImpl;
import com.hoainhi.staywise.validations.CapNhat;
import com.hoainhi.staywise.validations.ThemMoi;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import jakarta.validation.groups.Default;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/theloaitienich")
public class TheLoaiTienIchController {
    @Autowired
    private TheLoaiTienIchServiceImpl theLoaiTienIchService;

    @Autowired
    private Validator validator;

    @GetMapping("/search")
    public ResponseEntity<?> searchTenTheLoai(  @RequestParam(defaultValue = "") String tuKhoa,
                                                @RequestParam(defaultValue = "0") int trang,
                                                @RequestParam(defaultValue = "5") int kichThuoc){
        Page<TheLoaiTienIch> tienIches = theLoaiTienIchService.timKiem(tuKhoa, trang, kichThuoc);
       return ResponseEntity.ok(tienIches);
    }


    @PostMapping(
            value = "/save",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<?> save(
            @ModelAttribute TheLoaiTienTichDTO dto,
            @RequestParam(value = "file", required = false)
            MultipartFile file) throws IOException {

        Class<?> group = dto.getId() == null
                ? ThemMoi.class
                : CapNhat.class;


        Set<ConstraintViolation<TheLoaiTienTichDTO>> errors =
                validator.validate(dto, Default.class, group);

        if (!errors.isEmpty()) {
            List<String> messages = errors.stream()
                    .map(ConstraintViolation::getMessage)
                    .toList();

            return ResponseEntity.badRequest().body(messages);
        }

        String message = theLoaiTienIchService.saveTheLoai(dto, file);

        return ResponseEntity.ok(message);
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
