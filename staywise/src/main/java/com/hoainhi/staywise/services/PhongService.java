package com.hoainhi.staywise.services;

import com.hoainhi.staywise.dtos.ChiTietGiuongDTO;
import com.hoainhi.staywise.dtos.HomeStayDTO;
import com.hoainhi.staywise.dtos.PhongDTO;
import com.hoainhi.staywise.entities.Phong;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PhongService {

    Page<PhongDTO> showAll(String tuKhoa, int trang, int kichThuoc);
    void xoa(Long id);

    String savePhong(PhongDTO phongDTO);
}
