package com.hoainhi.staywise.services;

import com.hoainhi.staywise.dtos.CapNhatTrangThaiXacMinhDTO;
import com.hoainhi.staywise.dtos.NguoiDungDTO;
import com.hoainhi.staywise.dtos.NguoiDungResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public interface NguoiDungService {
    public void capNhatThongTin(String email, NguoiDungDTO dto) throws IOException;
    NguoiDungResponseDTO layThongTin(String email);
    void capNhatAnhDaiDien(
            String email,
            MultipartFile anhDaiDien) throws IOException;

    List<NguoiDungResponseDTO> getDanhSachOwner();

    void capNhatTrangThaiXacMinh(
            Long maNguoiDung,
            CapNhatTrangThaiXacMinhDTO dto
    );
}
