package com.hoainhi.staywise.services;

import com.hoainhi.staywise.dtos.*;
import com.hoainhi.staywise.enums.TrangThaiUser;
import com.hoainhi.staywise.enums.VaiTro;
import com.hoainhi.staywise.response.HoSoChuHomeStayResponseDTO;
import com.hoainhi.staywise.response.NguoiDungResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public interface NguoiDungService {
    public void capNhatThongTin(String email, NguoiDungDTO dto) throws IOException;
    NguoiDungResponseDTO layThongTin(String email);
    void capNhatAnhDaiDien(
            String email,
            MultipartFile anhDaiDien) throws IOException;

    Page<NguoiDungResponseDTO> getDanhSachOwner(
            String tuKhoa,
            int trang,
            int kichThuoc
    );

    void capNhatTrangThaiXacMinh(
            Long maNguoiDung,
            CapNhatTrangThaiXacMinhDTO dto
    );

    HoSoChuHomeStayResponseDTO getHoSoTheoNguoiDung(Long maNguoiDung);

    Page<NguoiDungResponseDTO> timKiemNguoiDung(
            String tuKhoa,
            VaiTro vaiTro,
            TrangThaiUser trangThai,
            int trang,
            int kichThuoc
    );

    void capNhatTrangThaiUser(
            Long id,
            CapNhatTrangThaiUserDTO dto
    );


}
