package com.hoainhi.staywise.services.serviceimpl;

import com.hoainhi.staywise.entities.TheLoaiTienIch;
import com.hoainhi.staywise.entities.TienIch;
import com.hoainhi.staywise.reponsitories.TheLoaiTienIchRepository;
import com.hoainhi.staywise.reponsitories.TienIchRepository;
import com.hoainhi.staywise.services.TienIchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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
}
