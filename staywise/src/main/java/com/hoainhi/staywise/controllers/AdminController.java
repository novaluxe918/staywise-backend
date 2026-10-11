package com.hoainhi.staywise.controllers;

import com.hoainhi.staywise.dtos.TuChoiHomeStayDTO;
import com.hoainhi.staywise.services.serviceimpl.HomeStayServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private HomeStayServiceImpl homeStayService;

    @PutMapping("/duyet/{id}")
    public ResponseEntity<?> duyetHomeStay(@PathVariable Long id){
        try{
            homeStayService.duyetHomeStay(id);
            return ResponseEntity.ok().body("Da duyet homestay");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/tuChoi")
    public ResponseEntity<?> tuChoiHomeStay(@RequestBody TuChoiHomeStayDTO tuChoiHomeStayDTO){
        try{
            homeStayService.tuChoiHomeStay(tuChoiHomeStayDTO.getId(), tuChoiHomeStayDTO.getLyDoTuChoi());
            return ResponseEntity.ok().body("Da gui thong tin");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/xoa/{id}")
    public ResponseEntity<?> xoaHomeStay(@PathVariable Long id){
        try{
            homeStayService.xoa(id);
            return ResponseEntity.ok().body("Da xoa thanh cong!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
