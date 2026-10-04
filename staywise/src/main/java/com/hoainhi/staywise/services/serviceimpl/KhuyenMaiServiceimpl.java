package com.hoainhi.staywise.services.serviceimpl;

import com.hoainhi.staywise.entities.KhuyenMai;
import com.hoainhi.staywise.reponsitories.KhuyenMaiRepository;
import com.hoainhi.staywise.services.KhuyenMaiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KhuyenMaiServiceimpl implements KhuyenMaiService {
    @Autowired
    private KhuyenMaiRepository khuyenMaiRepository;

    @Override
    public KhuyenMai themKhuyenMai(KhuyenMai khuyenMai) {
        return khuyenMaiRepository.save(khuyenMai);
    }
}
