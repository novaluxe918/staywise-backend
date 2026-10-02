package com.hoainhi.staywise.services;

import com.hoainhi.staywise.entities.TienIch;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public interface TienIchService {
    TienIch themTienIch(TienIch tienIch, Long maTheLoai);
    Page<TienIch> timKiemTienIch(String tuKhoa, int trang, int kichThuoc);
    void xoa(Long id);
}
