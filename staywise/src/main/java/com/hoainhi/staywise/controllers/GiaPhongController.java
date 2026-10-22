package com.hoainhi.staywise.controllers;

import com.hoainhi.staywise.dtos.GiaPhongDTO;
import com.hoainhi.staywise.services.serviceimpl.GiaPhongServiceImpl;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/giaPhong")
public class GiaPhongController {

    @Autowired
    private GiaPhongServiceImpl giaPhongService;

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

}
