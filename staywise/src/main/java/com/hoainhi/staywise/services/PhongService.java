package com.hoainhi.staywise.services;

import com.hoainhi.staywise.dtos.HomeStayDTO;
import com.hoainhi.staywise.dtos.PhongDTO;
import com.hoainhi.staywise.entities.Phong;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public interface PhongService {
    Phong themPhong(PhongDTO dto);
    Page<PhongDTO> showAll(String tuKhoa, int trang, int kichThuoc);
    void xoa(Long id);
    void capNhatPhong(Long id, PhongDTO dto);
}
