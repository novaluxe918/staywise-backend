package com.hoainhi.staywise.controllers;

import com.hoainhi.staywise.dtos.HomeStayDTO;
import com.hoainhi.staywise.dtos.HomeStayOptionDTO;
import com.hoainhi.staywise.response.TheLoaiHomeStayResponse;
import com.hoainhi.staywise.entities.NguoiDung;
import com.hoainhi.staywise.enums.TheLoaiHomeStay;
import com.hoainhi.staywise.enums.VaiTro;
import com.hoainhi.staywise.reponsitories.NguoiDungRepository;
import com.hoainhi.staywise.services.serviceimpl.HinhAnhHomeStaySeviceImpl;
import com.hoainhi.staywise.services.serviceimpl.HomeStayServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/homestay")
public class HomeStayController {
    @Autowired
    private HomeStayServiceImpl homeStayService;

    @Autowired
    private HinhAnhHomeStaySeviceImpl hinhAnhHomeStaySevice;

    @Autowired
    private NguoiDungRepository nguoiDungRepository;

    @GetMapping("/timKiem")
    public ResponseEntity<?> showAllHomeStay(@RequestParam(defaultValue = "") String tuKhoa,
                                             @RequestParam(defaultValue = "0") int trang,
                                             @RequestParam(defaultValue = "5") int kichThuoc){
        Page<HomeStayDTO> homeStays = homeStayService.showAll(tuKhoa, trang, kichThuoc);
        return ResponseEntity.ok(homeStays);
    }


    @PostMapping(value = "/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> save(
            @ModelAttribute HomeStayDTO dto,
            @RequestParam(value = "files", required = false)
            List<MultipartFile> files, Authentication authentication
           ) {


        try {
            if(authentication == null || !authentication.isAuthenticated()){
                 return ResponseEntity.status(401).body("Vui long dang nhap");
            }
            String email = authentication.getName();
            NguoiDung chuHomeStay = nguoiDungRepository.findByEmail(email).orElseThrow(() -> new RuntimeException(
                    "Không tìm thấy người dùng!"
            ));

            if(chuHomeStay.getVaiTro() != VaiTro.OWNER){
                return ResponseEntity.status(403).body("Ban khong co quyen dang ky homestay");
            }


              Long maChuHomeStay = chuHomeStay.getId();

            String message = homeStayService.saveHomeStay(
                    dto,
                    files,
                    maChuHomeStay
            );

            return ResponseEntity.ok(message);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @GetMapping("/theloai")
    public List<TheLoaiHomeStayResponse> getAll() {

        return Arrays.stream(TheLoaiHomeStay.values())
                .map(item -> new TheLoaiHomeStayResponse(
                        item.name(),
                        item.getLabel()
                ))
                .toList();
    }


    @GetMapping("/da-duyet")
    public ResponseEntity<?> layDanhSachHomeStayDaDuyet(
            Authentication authentication
    ) {

        if (authentication == null ||
                !authentication.isAuthenticated()) {

            return ResponseEntity.status(401)
                    .body("Vui lòng đăng nhập!");
        }

        List<HomeStayOptionDTO> danhSach =
                homeStayService.layDanhSachHomeStayDaDuyet(
                        authentication.getName()
                );

        return ResponseEntity.ok(danhSach);
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> layThongTinHomeStay(
            @PathVariable Long id,
            Authentication authentication
    ) {

        try {
            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(401)
                        .body("Vui lòng đăng nhập!");
            }

            HomeStayDTO dto = homeStayService.layThongTinHomeStay(
                    id,
                    authentication.getName()
            );

            return ResponseEntity.ok(dto);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}
