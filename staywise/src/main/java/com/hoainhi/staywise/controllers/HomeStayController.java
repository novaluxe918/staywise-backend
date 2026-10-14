package com.hoainhi.staywise.controllers;

import com.hoainhi.staywise.dtos.HomeStayDTO;
import com.hoainhi.staywise.services.serviceimpl.HinhAnhHomeStaySeviceImpl;
import com.hoainhi.staywise.services.serviceimpl.HomeStayServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("/homestay")
public class HomeStayController {
    @Autowired
    private HomeStayServiceImpl homeStayService;

    @Autowired
    private HinhAnhHomeStaySeviceImpl hinhAnhHomeStaySevice;

    @GetMapping("/timKiem")
    public ResponseEntity<?> showAllHomeStay(@RequestParam(defaultValue = "") String tuKhoa,
                                             @RequestParam(defaultValue = "0") int trang,
                                             @RequestParam(defaultValue = "5") int kichThuoc){
        Page<HomeStayDTO> homeStays = homeStayService.showAll(tuKhoa, trang, kichThuoc);
        return ResponseEntity.ok(homeStays);
    }


    @PostMapping(value = "/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> save(
            @Valid @ModelAttribute HomeStayDTO dto,
            @RequestParam(value = "files", required = false)
            List<MultipartFile> files) {

        try {
            String message = homeStayService.saveHomeStay(dto, files);
            return ResponseEntity.ok(message);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }



}
