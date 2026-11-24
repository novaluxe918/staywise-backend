package com.hoainhi.staywise.controllers;

import com.hoainhi.staywise.dtos.DuDoanGiaRequest;
import com.hoainhi.staywise.response.DuDoanGiaResponse;
import com.hoainhi.staywise.response.LichSuDuDoanGiaResponse;
import com.hoainhi.staywise.services.serviceimpl.DuDoanGiaServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/du-doan-gia")
public class DuDoanGiaController {
    @Autowired
    private DuDoanGiaServiceImpl duDoanGiaService;

    @PostMapping("/du-doan")
    public ResponseEntity<DuDoanGiaResponse> duDoanGia(
            @Valid @RequestBody DuDoanGiaRequest request
    ) {
        return ResponseEntity.ok(
                duDoanGiaService.duDoanGia(request)
        );
    }


    @GetMapping("/lich-su")
    public ResponseEntity<List<LichSuDuDoanGiaResponse>> xemLichSu() {

        return ResponseEntity.ok(
                duDoanGiaService.xemLichSuDuDoanGia()
        );
    }

}
