package com.hoainhi.staywise.services;

import com.hoainhi.staywise.dtos.DangKyDTO;
import com.hoainhi.staywise.entities.NguoiDung;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {
  NguoiDung dangKy(DangKyDTO dangKyDTO);
}
