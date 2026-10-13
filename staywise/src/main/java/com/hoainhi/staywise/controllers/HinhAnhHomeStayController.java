package com.hoainhi.staywise.controllers;

import com.hoainhi.staywise.entities.HinhAnhHomeStay;
import com.hoainhi.staywise.services.serviceimpl.HinhAnhHomeStaySeviceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/hinhAnh")
public class HinhAnhHomeStayController {
    @Autowired
    private HinhAnhHomeStaySeviceImpl hinhAnhHomeStaySevice;

    @PostMapping("/upload/{maHomeStay}")
    public ResponseEntity<?> uploadAnh(@PathVariable Long maHomeStay, @RequestParam("files")List<MultipartFile> files) throws IOException{
       return ResponseEntity.ok(hinhAnhHomeStaySevice.themAnh(files, maHomeStay));
    }
}
