package com.hoainhi.staywise.services;

import com.hoainhi.staywise.entities.KhuyenMai;
import org.springframework.stereotype.Service;

@Service
public interface KhuyenMaiService {
    KhuyenMai themKhuyenMai(KhuyenMai khuyenMai);
}
