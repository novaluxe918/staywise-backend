package com.hoainhi.staywise.services;

import com.hoainhi.staywise.entities.LoaiGiuong;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public interface LoaiGiuongService {
    LoaiGiuong themLoaiGiuong(LoaiGiuong loaiGiuong);
    Page<LoaiGiuong> timKiem(String tuKhoa, int trang, int kichThuoc);
    void xoa(Long id);
}

