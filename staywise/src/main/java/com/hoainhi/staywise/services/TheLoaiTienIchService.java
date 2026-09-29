package com.hoainhi.staywise.services;

import com.hoainhi.staywise.entities.TheLoaiTienIch;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public interface TheLoaiTienIchService {
    TheLoaiTienIch themTheLoai(TheLoaiTienIch theLoaiTienIch );
    Page<TheLoaiTienIch> timKiem(String tuKhoa, int trang, int kichThuoc);
}
