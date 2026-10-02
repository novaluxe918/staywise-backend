package com.hoainhi.staywise.services.serviceimpl;

import com.hoainhi.staywise.entities.DichVu;
import com.hoainhi.staywise.reponsitories.DichVuRepository;
import com.hoainhi.staywise.services.DichVuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DichVuServiceImpl implements DichVuService {
    @Autowired
    private DichVuRepository dichVuRepository;
    @Override
    public DichVu themDichVu(DichVu dichVu) {
        return dichVuRepository.save(dichVu);
    }
}
