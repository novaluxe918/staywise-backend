package com.hoainhi.staywise.services.serviceimpl;

import com.hoainhi.staywise.dtos.DangKyDTO;
import com.hoainhi.staywise.dtos.DangNhapDTO;
import com.hoainhi.staywise.dtos.DangNhapResponse;
import com.hoainhi.staywise.entities.NguoiDung;
import com.hoainhi.staywise.entities.TokenDaThuHoi;
import com.hoainhi.staywise.enums.TrangThaiUser;
import com.hoainhi.staywise.enums.VaiTro;
import com.hoainhi.staywise.reponsitories.NguoiDungRepository;
import com.hoainhi.staywise.reponsitories.TokenDaThuHoiRepository;
import com.hoainhi.staywise.security.JwtService;
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
    private TokenDaThuHoiRepository tokenDaThuHoiRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Override
    public NguoiDung dangKy(DangKyDTO dangKyDTO) {
        if(nguoiDungRepository.existsByEmail(dangKyDTO.getEmail())){
            throw new RuntimeException("Email da ton tai!");
        }

        if (!dangKyDTO.getMatKhau().equals(dangKyDTO.getXacNhanMatKhau())) {
            throw new RuntimeException("Mật khẩu xác nhận không khớp!");
        }

        VaiTro vaiTro = dangKyDTO.getVaiTro();
         if(vaiTro == null || (vaiTro != VaiTro.USER && vaiTro != VaiTro.OWNER)){
              throw new RuntimeException("Vai tro dang ky khong hop le");

         }

         NguoiDung nguoiDung = new NguoiDung();
        nguoiDung.setHoTen(dangKyDTO.getHoTen());
        nguoiDung.setEmail(dangKyDTO.getEmail());
        nguoiDung.setSoDienThoai(dangKyDTO.getSoDienThoai());
        nguoiDung.setVaiTro(vaiTro);
        nguoiDung.setMatKhau(passwordEncoder.encode(dangKyDTO.getMatKhau()));
        nguoiDung.setTrangThaiUser(TrangThaiUser.DANG_HOAT_DONG);
        nguoiDung.setNgayTao(LocalDateTime.now());
        return nguoiDungRepository.save(nguoiDung);
    }

    @Override
    public DangNhapResponse dangNhap(DangNhapDTO dangNhapDTO) {
        NguoiDung nguoiDung = nguoiDungRepository.findByEmail(dangNhapDTO.getEmail()).orElseThrow(() -> new RuntimeException("Email hoặc mật khẩu không chính xác!"));
        if(!passwordEncoder.matches(dangNhapDTO.getMatKhau(), nguoiDung.getMatKhau())){
            throw new RuntimeException("Email hoặc mật khẩu không chính xác!");
        }
        if(nguoiDung.getTrangThaiUser() != TrangThaiUser.DANG_HOAT_DONG){
            throw new RuntimeException("Tai khoan khong hoat dong!");

        }
        String token = jwtService.taoToken(nguoiDung.getEmail());

      return new DangNhapResponse(
              token,
              nguoiDung.getId(),
              nguoiDung.getEmail(),
              nguoiDung.getVaiTro().name(),
              nguoiDung.getHoTen()
      );
    }

    @Override
    public void dangXuat(String token) {
        if(tokenDaThuHoiRepository.existsByToken(token)){
            return;
        }

        LocalDateTime ngayHetHan = jwtService.layThoiDiemHetHan(token);
        TokenDaThuHoi tokenDaThuHoi = new TokenDaThuHoi();
        tokenDaThuHoi.setToken(token);
        tokenDaThuHoi.setNgayHetHan(ngayHetHan);

        tokenDaThuHoiRepository.save(tokenDaThuHoi);
    }
}
