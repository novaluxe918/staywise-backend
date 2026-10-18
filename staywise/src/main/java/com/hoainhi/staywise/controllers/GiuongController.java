package com.hoainhi.staywise.controllers;

import com.hoainhi.staywise.dtos.LoaiGiuongDTO;
import com.hoainhi.staywise.entities.LoaiGiuong;
import com.hoainhi.staywise.services.serviceimpl.LoaiGiuongServiceImpl;
import com.hoainhi.staywise.validations.CapNhat;
import com.hoainhi.staywise.validations.ThemMoi;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.validation.groups.Default;
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
@RequestMapping("/giuong")
public class GiuongController {

    @Autowired
    private LoaiGiuongServiceImpl loaiGiuongService;

    @Autowired
    private Validator validator;

    @GetMapping("/timkiem")
    public ResponseEntity<?> timKiem( @RequestParam(defaultValue = "") String tuKhoa,
                                      @RequestParam(defaultValue = "0") int trang,  @RequestParam(defaultValue = "5") int kichThuoc){
        Page<LoaiGiuong> loaiGiuongs = loaiGiuongService.timKiem(tuKhoa, trang, kichThuoc);
        return ResponseEntity.ok(loaiGiuongs);
    }

    @PostMapping(
            value = "/save",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<?> themLoaiGiuong(@ModelAttribute LoaiGiuongDTO loaiGiuongDTO,
                                            @RequestParam(value = "file", required = false)
    MultipartFile file) throws IOException {

        Class<?> group = loaiGiuongDTO.getId() == null
                ? ThemMoi.class
                : CapNhat.class;


        Set<ConstraintViolation<LoaiGiuongDTO>> errors =
                validator.validate(loaiGiuongDTO, Default.class, group);

        if (!errors.isEmpty()) {
            List<String> messages = errors.stream()
                    .map(ConstraintViolation::getMessage)
                    .toList();

            return ResponseEntity.badRequest().body(messages);
        }
        String message = loaiGiuongService.saveLoaiGiuong(loaiGiuongDTO, file);
        return ResponseEntity.ok(message);
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
