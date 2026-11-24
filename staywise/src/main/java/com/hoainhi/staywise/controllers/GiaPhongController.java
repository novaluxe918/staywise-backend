package com.hoainhi.staywise.controllers;

import com.hoainhi.staywise.dtos.GiaPhongDTO;
import com.hoainhi.staywise.services.serviceimpl.GiaPhongServiceImpl;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/giaPhong")
public class GiaPhongController {

    @Autowired
    private GiaPhongServiceImpl giaPhongService;

    @GetMapping("/show-all")
    public ResponseEntity<Page<GiaPhongDTO>> showAllGiaPhong(

            @RequestParam(defaultValue = "") String tuKhoa,

            @RequestParam(defaultValue = "0") int trang,

            @RequestParam(defaultValue = "5") int kichThuoc

    ) {

        return ResponseEntity.ok(
                giaPhongService.showAllGiaPhong(tuKhoa, trang, kichThuoc)
        );
    }

    @PostMapping(value = "/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> themGiaPhong(@ModelAttribute GiaPhongDTO giaPhongDTO){
        try{
            String message = giaPhongService.saveGiaPhong(giaPhongDTO);
             return ResponseEntity.ok(message);
        } catch (ConstraintViolationException e) {
            return ResponseEntity.badRequest().body(e.getMessage());

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/xoa/{id}")
    public ResponseEntity<String> xoa(@PathVariable Long id) {
        giaPhongService.xoa(id);
        return ResponseEntity.ok("Xóa giá phòng thành công");
    }


}
