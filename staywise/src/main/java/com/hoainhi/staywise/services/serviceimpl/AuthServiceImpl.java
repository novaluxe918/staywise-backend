package com.hoainhi.staywise.services.serviceimpl;

import com.hoainhi.staywise.dtos.DangKyDTO;
import com.hoainhi.staywise.entities.NguoiDung;
import com.hoainhi.staywise.enums.TrangThaiUser;
import com.hoainhi.staywise.reponsitories.NguoiDungRepository;
import com.hoainhi.staywise.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private NguoiDungRepository nguoiDungRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public NguoiDung dangKy(DangKyDTO dangKyDTO) {
        if(nguoiDungRepository.existsByEmail(dangKyDTO.getEmail())){
            throw new RuntimeException("Email da ton tai!");
        }

        NguoiDung nguoiDung = new NguoiDung();
        nguoiDung.setHoTen(dangKyDTO.getHoTen());
        nguoiDung.setEmail(dangKyDTO.getEmail());
        nguoiDung.setSoDienThoai(dangKyDTO.getSoDienThoai());
        nguoiDung.setMatKhau(passwordEncoder.encode(dangKyDTO.getMatKhau()));
        nguoiDung.setTrangThaiUser(TrangThaiUser.DANG_HOAT_DONG);
        nguoiDung.setVaiTro(dangKyDTO.getVaiTro());
        nguoiDung.setNgayTao(LocalDateTime.now());
        return nguoiDungRepository.save(nguoiDung);
    }
}
