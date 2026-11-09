package com.hoainhi.staywise.services.serviceimpl;

import com.hoainhi.staywise.dtos.NguoiDungDTO;
import com.hoainhi.staywise.dtos.NguoiDungResponseDTO;
import com.hoainhi.staywise.entities.NguoiDung;
import com.hoainhi.staywise.enums.TrangThaiXacMinh;
import com.hoainhi.staywise.reponsitories.NguoiDungRepository;
import com.hoainhi.staywise.services.NguoiDungService;
import com.hoainhi.staywise.services.cloudinary.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
public class NguoiDungServiceImpl implements NguoiDungService {
    @Autowired
    private NguoiDungRepository nguoiDungRepository;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Override
    @Transactional
    public void capNhatThongTin(String email, NguoiDungDTO dto) throws IOException {

        NguoiDung nd = nguoiDungRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        // Cập nhật thông tin cá nhân
        nd.setHoTen(dto.getHoTen());

        nd.setSoDienThoai(
                dto.getSoDienThoai() == null || dto.getSoDienThoai().isBlank()
                        ? null
                        : dto.getSoDienThoai()
        );

        // Kiểm tra thay đổi giấy tờ
        boolean thayDoiGiayTo = false;

        // Ảnh đại diện
        if (dto.getAnhDaiDien() != null && !dto.getAnhDaiDien().isEmpty()) {
            nd.setAnhDaiDien(
                    cloudinaryService.taiAnh(dto.getAnhDaiDien())
            );
        }

        // CCCD mặt trước
        if (dto.getAnhCCCDMatTruoc() != null
                && !dto.getAnhCCCDMatTruoc().isEmpty()) {

            nd.setAnhCCCDMatTruoc(
                    cloudinaryService.taiAnh(dto.getAnhCCCDMatTruoc())
            );

            thayDoiGiayTo = true;
        }

        // CCCD mặt sau
        if (dto.getAnhCCCDMatSau() != null
                && !dto.getAnhCCCDMatSau().isEmpty()) {

            nd.setAnhCCCDMatSau(
                    cloudinaryService.taiAnh(dto.getAnhCCCDMatSau())
            );

            thayDoiGiayTo = true;
        }

        // Giấy phép kinh doanh
        if (dto.getAnhGiayPhepKinhDoanh() != null
                && !dto.getAnhGiayPhepKinhDoanh().isEmpty()) {

            nd.setAnhGiayPhepKinhDoanh(
                    cloudinaryService.taiAnh(dto.getAnhGiayPhepKinhDoanh())
            );

            thayDoiGiayTo = true;
        }

        if (thayDoiGiayTo) {
            nd.setTrangThaiXacMinh(TrangThaiXacMinh.CHUA_XAC_MINH);
            nd.setNgayXacMinh(null);
            nd.setLyDoTuChoiXacMinh(null);
        }

        nguoiDungRepository.save(nd);
    }

    @Override
    public NguoiDungResponseDTO layThongTin(String email) {
        NguoiDung nd = nguoiDungRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        NguoiDungResponseDTO dto = new NguoiDungResponseDTO();

        dto.setHoTen(nd.getHoTen());
        dto.setEmail(nd.getEmail());
        dto.setSoDienThoai(nd.getSoDienThoai());

        dto.setAnhDaiDien(nd.getAnhDaiDien());
        dto.setAnhCCCDMatTruoc(nd.getAnhCCCDMatTruoc());
        dto.setAnhCCCDMatSau(nd.getAnhCCCDMatSau());
        dto.setAnhGiayPhepKinhDoanh(nd.getAnhGiayPhepKinhDoanh());

        dto.setTrangThaiXacMinh(nd.getTrangThaiXacMinh());
        dto.setLyDoTuChoiXacMinh(nd.getLyDoTuChoiXacMinh());

        return dto;
    }
}
