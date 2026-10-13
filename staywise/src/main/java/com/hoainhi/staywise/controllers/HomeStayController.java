package com.hoainhi.staywise.controllers;

import com.hoainhi.staywise.dtos.HomeStayDTO;
import com.hoainhi.staywise.services.serviceimpl.HomeStayServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/homestay")
public class HomeStayController {
    @Autowired
    private HomeStayServiceImpl homeStayService;

    @GetMapping("/timKiem")
    public ResponseEntity<?> showAllHomeStay(@RequestParam(defaultValue = "") String tuKhoa,
                                             @RequestParam(defaultValue = "0") int trang,
                                             @RequestParam(defaultValue = "5") int kichThuoc){
        Page<HomeStayDTO> homeStays = homeStayService.showAll(tuKhoa, trang, kichThuoc);
        return ResponseEntity.ok(homeStays);
    }


    @PostMapping("/save")
    public ResponseEntity<?> themHomeStay(
            @Valid @RequestBody HomeStayDTO homeStayDTO) {

        boolean themMoi = homeStayDTO.getId() == null;

        try {
            if (themMoi) {
                homeStayService.themHomeStay(
                        homeStayDTO,
                        homeStayDTO.getMaChuHomeStay()
                );

                return ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body("Đăng ký HomeStay thành công, đang chờ Admin phê duyệt!");

            } else {
                homeStayService.capNhatHomeStay(
                        homeStayDTO.getId(),
                        homeStayDTO
                );

                return ResponseEntity
                        .ok("Cập nhật thông tin HomeStay thành công!");
            }

        } catch (RuntimeException e) {
            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }





}
