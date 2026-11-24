package com.hoainhi.staywise.services;

import com.hoainhi.staywise.dtos.GiaPhongDTO;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public interface GiaPhongService {
    String saveGiaPhong(GiaPhongDTO giaPhongDTO);
    Page<GiaPhongDTO> showAllGiaPhong(
            String tuKhoa,
            int trang,
            int kichThuoc
    );

    void xoa(Long id);
}
