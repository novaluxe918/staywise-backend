package com.hoainhi.staywise.services;

import com.hoainhi.staywise.dtos.LoaiGiuongDTO;
import com.hoainhi.staywise.entities.LoaiGiuong;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public interface LoaiGiuongService {
    LoaiGiuong themLoaiGiuong(LoaiGiuong loaiGiuong);
    Page<LoaiGiuong> timKiem(String tuKhoa, int trang, int kichThuoc);
    void xoa(Long id);
    LoaiGiuong getById(Long id);
    String saveLoaiGiuong(LoaiGiuongDTO loaiGiuongDTO, MultipartFile file) throws IOException;
}

