package com.hoainhi.staywise.services;

import com.hoainhi.staywise.dtos.DichVuDTO;
import com.hoainhi.staywise.entities.DichVu;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public interface DichVuService {
    DichVu themDichVu(DichVu dichVu);
    Page<DichVu> timKiem(String tuKhoa, int trang, int kichThuoc);
    void xoa(Long id);
    DichVu getById(Long id);
    String saveDichVu(DichVuDTO dichVuDTO);
}
