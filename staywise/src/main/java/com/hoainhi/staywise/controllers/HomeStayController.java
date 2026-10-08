package com.hoainhi.staywise.controllers;

import com.hoainhi.staywise.dtos.HomeStayDTO;
import com.hoainhi.staywise.entities.HomeStay;
import com.hoainhi.staywise.services.serviceimpl.HomeStayServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/homestay")
public class HomeStayController {
    @Autowired
    private HomeStayServiceImpl homeStayService;

    @PostMapping("/save")
    public ResponseEntity<?> themHomeStay(@Valid @RequestBody HomeStayDTO homeStayDTO){
        try{
           homeStayService.themHomeStay(homeStayDTO, homeStayDTO.getMaChuHomeStay());
           return ResponseEntity.status(HttpStatus.CREATED).body("Dang ky thanh cong, cho phe duyet");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("dang ky that bai!" +   e.getMessage());
        }
    }
}
