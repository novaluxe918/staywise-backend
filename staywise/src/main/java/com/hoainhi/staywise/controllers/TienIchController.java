package com.hoainhi.staywise.controllers;

import com.hoainhi.staywise.dtos.TienIchDTO;
import com.hoainhi.staywise.entities.TheLoaiTienIch;
import com.hoainhi.staywise.entities.TienIch;
import com.hoainhi.staywise.services.serviceimpl.TienIchServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/tienich")
public class TienIchController {
    @Autowired
    private TienIchServiceImpl tienIchService;

    @PostMapping("/save")
    public ResponseEntity<?> saveTienIch(@RequestBody TienIchDTO tienIchDTO){
        try {
            TienIch tienIch = new TienIch();
            BeanUtils.copyProperties(tienIchDTO, tienIch);
            tienIchService.themTienIch(tienIch, tienIchDTO.getMatheloai());
            return ResponseEntity.ok("them thanh cong!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Them that bai!");
        }
    }
}
