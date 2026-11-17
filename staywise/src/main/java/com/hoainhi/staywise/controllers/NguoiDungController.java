package com.hoainhi.staywise.controllers;

import com.hoainhi.staywise.dtos.NguoiDungDTO;
import com.hoainhi.staywise.dtos.NguoiDungResponseDTO;
import com.hoainhi.staywise.entities.NguoiDung;
import com.hoainhi.staywise.reponsitories.NguoiDungRepository;
import com.hoainhi.staywise.services.serviceimpl.NguoiDungServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/nguoidung")
public class NguoiDungController {
    @Autowired
    private NguoiDungServiceImpl nguoiDungService;

    @Autowired
    private NguoiDungRepository nguoiDungRepository;

    @PutMapping(
            value = "/cap-nhat",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<?> capNhatThongTin(
            @Valid @ModelAttribute NguoiDungDTO dto,
            Authentication authentication) throws IOException {

        String email = authentication.getName();

        nguoiDungService.capNhatThongTin(email, dto);

        return ResponseEntity.ok("Cập nhật thông tin thành công");
    }

    @GetMapping("/thong-tin")
    public ResponseEntity<NguoiDungResponseDTO> layThongTin(
            Authentication authentication) {

        return ResponseEntity.ok(
                nguoiDungService.layThongTin(authentication.getName())
        );
    }

    @PutMapping("/anhDaiDien")
    public ResponseEntity<?> capNhatAnhDaiDien(
            @RequestParam("anhDaiDien") MultipartFile anhDaiDien,
            Authentication authentication
    ) throws IOException {

        nguoiDungService.capNhatAnhDaiDien(
                authentication.getName(),
                anhDaiDien
        );

        return ResponseEntity.ok("Cập nhật ảnh đại diện thành công");
    }

    @GetMapping("/thong-bao")
    public ResponseEntity<?> getThongTinHoSo(Authentication authentication) {

        NguoiDung nguoiDung = nguoiDungRepository
                .findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        return ResponseEntity.ok(
                nguoiDungService.getHoSoTheoNguoiDung(nguoiDung.getId())
        );
    }
}
