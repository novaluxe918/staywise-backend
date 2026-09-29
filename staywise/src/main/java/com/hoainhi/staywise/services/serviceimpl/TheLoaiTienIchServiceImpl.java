package com.hoainhi.staywise.services.serviceimpl;

import com.hoainhi.staywise.entities.TheLoaiTienIch;
import com.hoainhi.staywise.reponsitories.TheLoaiTienIchRepository;
import com.hoainhi.staywise.services.TheLoaiTienIchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class TheLoaiTienIchServiceImpl implements TheLoaiTienIchService {
    @Autowired
    private TheLoaiTienIchRepository theLoaiTienIchRepository;

    @Override
    public TheLoaiTienIch themTheLoai(TheLoaiTienIch theLoaiTienIch) {
        return theLoaiTienIchRepository.save(theLoaiTienIch);
    }

    @Override
    public Page<TheLoaiTienIch> timKiem(String tuKhoa, int trang, int kichThuoc) {
        Pageable pageable = PageRequest.of(trang, kichThuoc, Sort.by("id").descending());
        if(tuKhoa == null || "".equals(tuKhoa.trim())){
           return theLoaiTienIchRepository.findAll(pageable);
        }
        return theLoaiTienIchRepository.findByTenTheLoaiContainingIgnoreCase(tuKhoa, pageable);
    }
}
