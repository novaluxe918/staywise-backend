package com.hoainhi.staywise.services.serviceimpl;

import com.hoainhi.staywise.dtos.DangKyDTO;
import com.hoainhi.staywise.dtos.DangNhapDTO;
import com.hoainhi.staywise.response.DangNhapResponse;
import com.hoainhi.staywise.entities.HoSoChuHomeStay;
import com.hoainhi.staywise.entities.NguoiDung;
import com.hoainhi.staywise.enums.TrangThaiUser;
import com.hoainhi.staywise.enums.TrangThaiXacMinh;
import com.hoainhi.staywise.enums.VaiTro;
import com.hoainhi.staywise.reponsitories.HoSoChuHomeStayRepository;
import com.hoainhi.staywise.reponsitories.NguoiDungRepository;
import com.hoainhi.staywise.security.JwtService;
import com.hoainhi.staywise.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private NguoiDungRepository nguoiDungRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private HoSoChuHomeStayRepository hoSoChuHomeStayRepository;


    @Override
    @Transactional
    public NguoiDung dangKy(DangKyDTO dangKyDTO) {

        if (nguoiDungRepository.existsByEmail(dangKyDTO.getEmail())) {
            throw new RuntimeException("Email đã tồn tại!");
        }

        if (!dangKyDTO.getMatKhau().equals(dangKyDTO.getXacNhanMatKhau())) {
            throw new RuntimeException("Mật khẩu xác nhận không khớp!");
        }


        VaiTro vaiTro = dangKyDTO.getVaiTro();

        if (vaiTro == null || (vaiTro != VaiTro.USER && vaiTro != VaiTro.OWNER)) {
            throw new RuntimeException("Vai trò đăng ký không hợp lệ");
        }


        NguoiDung nguoiDung = new NguoiDung();

        nguoiDung.setHoTen(dangKyDTO.getHoTen());
        nguoiDung.setEmail(dangKyDTO.getEmail());
        nguoiDung.setSoDienThoai(dangKyDTO.getSoDienThoai());
        nguoiDung.setVaiTro(vaiTro);
        nguoiDung.setMatKhau(passwordEncoder.encode(dangKyDTO.getMatKhau()));
        nguoiDung.setTrangThaiUser(TrangThaiUser.DANG_HOAT_DONG);
        nguoiDung.setNgayTao(LocalDateTime.now());


        NguoiDung nguoiDungDaLuu = nguoiDungRepository.save(nguoiDung);


        if (vaiTro == VaiTro.OWNER) {

            HoSoChuHomeStay hoSo = new HoSoChuHomeStay();


            hoSo.setNguoiDung(nguoiDungDaLuu);


            hoSo.setTrangThaiXacMinh(TrangThaiXacMinh.CHUA_XAC_MINH);


            hoSoChuHomeStayRepository.save(hoSo);
        }

        return nguoiDungDaLuu;
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
              nguoiDung.getHoTen(),
              nguoiDung.getAnhDaiDien()
      );
    }


}
