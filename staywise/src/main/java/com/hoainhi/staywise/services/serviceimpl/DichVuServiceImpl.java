package com.hoainhi.staywise.services.serviceimpl;

import com.hoainhi.staywise.entities.DichVu;
import com.hoainhi.staywise.enums.TrangThai;
import com.hoainhi.staywise.reponsitories.DichVuRepository;
import com.hoainhi.staywise.services.DichVuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class DichVuServiceImpl implements DichVuService {
    @Autowired
    private DichVuRepository dichVuRepository;
    @Override
    public DichVu themDichVu(DichVu dichVu) {
        return dichVuRepository.save(dichVu);
    }

    @Override
    public Page<DichVu> timKiem(String tuKhoa, int trang, int kichThuoc) {
        Pageable pageable = PageRequest.of(trang, kichThuoc, Sort.by("id").descending());
        if(tuKhoa == null || "".equals(tuKhoa.trim())){
            return dichVuRepository.findByTrangThaiNot(TrangThai.DA_XOA, pageable);
        }
        return dichVuRepository.finByTenDichVuContainingIgnoreCase(tuKhoa, pageable, TrangThai.DA_XOA);
    }

    @Override
    public void xoa(Long id) {
        DichVu dichVu = dichVuRepository.findById(id).orElseThrow(() -> new RuntimeException(
                "Không tìm thấy thể loại tiện ích"
        ));
        dichVu.setTrangThai(TrangThai.DA_XOA);
        dichVuRepository.save(dichVu);

    }
}
