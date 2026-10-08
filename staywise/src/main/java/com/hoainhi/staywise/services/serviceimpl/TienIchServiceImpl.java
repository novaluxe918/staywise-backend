package com.hoainhi.staywise.services.serviceimpl;

import com.hoainhi.staywise.entities.TheLoaiTienIch;
import com.hoainhi.staywise.entities.TienIch;
import com.hoainhi.staywise.enums.TrangThai;
import com.hoainhi.staywise.reponsitories.TheLoaiTienIchRepository;
import com.hoainhi.staywise.reponsitories.TienIchRepository;
import com.hoainhi.staywise.services.TienIchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class TienIchServiceImpl implements TienIchService {
    @Autowired
    private TienIchRepository tienIchRepository;

    @Autowired
    private TheLoaiTienIchRepository theLoaiTienIchRepository;
    @Override
    public TienIch themTienIch(TienIch tienIch, Long maTheLoai) {
        TheLoaiTienIch theLoai = theLoaiTienIchRepository
                .findById(maTheLoai)
                .orElseThrow(() ->
                        new RuntimeException("Không tìm thấy thể loại tiện ích"));

        tienIch.setTheLoaiTienIch(theLoai);
        tienIch.setNgayTao(LocalDateTime.now());
        return tienIchRepository.save(tienIch);
    }

    @Override
    public Page<TienIch> timKiemTienIch(String tuKhoa, int trang, int kichThuoc) {
        Pageable pageable = PageRequest.of(trang, kichThuoc, Sort.by("id").descending());
        if(tuKhoa == null || "".equals(tuKhoa.trim())){
            return tienIchRepository.findByTrangThaiNot(TrangThai.DA_XOA, pageable);
        }
        return tienIchRepository.findByTenTienIchContainingIgnoreCase(tuKhoa, pageable, TrangThai.DA_XOA);
    }

    @Override
    public void xoa(Long id) {
        TienIch tienIch = tienIchRepository.findById(id).orElseThrow();
        tienIch.setTrangThai(TrangThai.DA_XOA);
        tienIchRepository.save(tienIch);
    }

    @Override
    public Optional<TienIch> findById(Long id) {
        return tienIchRepository.findById(id);
    }
}
