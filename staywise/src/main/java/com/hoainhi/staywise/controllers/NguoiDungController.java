package com.hoainhi.staywise.controllers;

import com.hoainhi.staywise.dtos.NguoiDungDTO;
import com.hoainhi.staywise.dtos.NguoiDungResponseDTO;
import com.hoainhi.staywise.services.serviceimpl.NguoiDungServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Controller
@RequestMapping("/nguoidung")
public class NguoiDungController {
    private NguoiDungServiceImpl nguoiDungService;
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
}
