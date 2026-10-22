package com.hoainhi.staywise.services;

import com.hoainhi.staywise.dtos.GiaPhongDTO;
import org.springframework.stereotype.Service;

@Service
public interface GiaPhongService {
    String saveGiaPhong(GiaPhongDTO giaPhongDTO);
}
