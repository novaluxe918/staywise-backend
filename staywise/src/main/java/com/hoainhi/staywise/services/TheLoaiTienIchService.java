package com.hoainhi.staywise.services;

import com.hoainhi.staywise.dtos.TheLoaiTienTichDTO;
import com.hoainhi.staywise.entities.TheLoaiTienIch;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public interface TheLoaiTienIchService {
    TheLoaiTienIch themTheLoai(TheLoaiTienIch theLoaiTienIch );
    Page<TheLoaiTienIch> timKiem(String tuKhoa, int trang, int kichThuoc);
    void xoa(Long id);
    TheLoaiTienIch getById(Long id);
    String saveTheLoai(TheLoaiTienTichDTO theLoaiTienTichDTO, MultipartFile file) throws IOException;
}
