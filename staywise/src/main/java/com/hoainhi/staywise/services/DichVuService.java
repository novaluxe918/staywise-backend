package com.hoainhi.staywise.services;

import com.hoainhi.staywise.entities.DichVu;
import org.springframework.stereotype.Service;

@Service
public interface DichVuService {
    DichVu themDichVu(DichVu dichVu);
}
