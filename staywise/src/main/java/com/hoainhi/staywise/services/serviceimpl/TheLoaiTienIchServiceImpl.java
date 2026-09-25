package com.hoainhi.staywise.services.serviceimpl;

import com.hoainhi.staywise.entities.TheLoaiTienIch;
import com.hoainhi.staywise.reponsitories.TheLoaiTienIchRepository;
import com.hoainhi.staywise.services.TheLoaiTienIchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TheLoaiTienIchServiceImpl implements TheLoaiTienIchService {
    @Autowired
    private TheLoaiTienIchRepository theLoaiTienIchRepository;

    @Override
    public TheLoaiTienIch themTheLoai(TheLoaiTienIch theLoaiTienIch) {
        return theLoaiTienIchRepository.save(theLoaiTienIch);
    }
}
