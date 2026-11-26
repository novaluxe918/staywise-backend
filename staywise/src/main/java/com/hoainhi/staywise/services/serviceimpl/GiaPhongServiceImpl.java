package com.hoainhi.staywise.services.serviceimpl;

import com.hoainhi.staywise.dtos.GiaPhongDTO;
import com.hoainhi.staywise.entities.GiaPhong;
import com.hoainhi.staywise.entities.Phong;
import com.hoainhi.staywise.enums.TrangThai;
import com.hoainhi.staywise.reponsitories.GiaPhongRepository;
import com.hoainhi.staywise.reponsitories.PhongRepository;
import com.hoainhi.staywise.services.GiaPhongService;
import com.hoainhi.staywise.validations.CapNhat;
import com.hoainhi.staywise.validations.ThemMoi;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Service
public class GiaPhongServiceImpl implements GiaPhongService {
    @Autowired
    private GiaPhongRepository giaPhongRepository;

    @Autowired
    private PhongRepository phongRepository;

    @Autowired
    private Validator validator;

    @Override
    public String saveGiaPhong(GiaPhongDTO giaPhongDTO) {
        boolean themMoi= giaPhongDTO.getId() == null;

        Set<ConstraintViolation<GiaPhongDTO>> violations =
                themMoi
                        ? validator.validate(giaPhongDTO, ThemMoi.class)
                        : validator.validate(giaPhongDTO, CapNhat.class);

        if (!violations.isEmpty()) {
            return violations.iterator().next().getMessage();
        }

        GiaPhong giaPhong;

        if(themMoi){
            giaPhong = new GiaPhong();
            giaPhong.setTrangThai(TrangThai.DANG_HOAT_DONG);

        }else {
            giaPhong = giaPhongRepository.findById(giaPhongDTO.getId()).orElseThrow(() ->
                    new RuntimeException("Không tìm thấy giá phòng"));
        }
        if (giaPhongDTO.getMaPhong() != null) {
            Phong phong = phongRepository.findById(giaPhongDTO.getMaPhong())
                    .orElseThrow(() ->
                            new RuntimeException("Không tìm thấy phòng"));

            giaPhong.setPhong(phong);
        }

        if (giaPhongDTO.getGiaPhong() != null) {
            if (giaPhongDTO.getGiaPhong().compareTo(BigDecimal.ZERO) <= 0) {
                return "Giá phòng phải lớn hơn 0";
            }
            giaPhong.setGiaPhong(giaPhongDTO.getGiaPhong());
        }


        if (giaPhongDTO.getNgayBatDau() != null) {
            giaPhong.setNgayBatDau(giaPhongDTO.getNgayBatDau());
        }


        if (giaPhongDTO.getNgayKetThuc() != null) {
            giaPhong.setNgayKetThuc(giaPhongDTO.getNgayKetThuc());
        }

        if (giaPhong.getNgayBatDau() != null
                && giaPhong.getNgayKetThuc() != null
                && !giaPhong.getNgayKetThuc()
                .isAfter(giaPhong.getNgayBatDau())) {
            return "Ngày kết thúc phải sau ngày bắt đầu";
        }

        giaPhongRepository.save(giaPhong);
        return themMoi ? "Them thong tin thanh cong" : "Cap nhat thong tin thanh cong";
    }

    @Override
    public Page<GiaPhongDTO> showAllGiaPhong(
            String tuKhoa,
            int trang,
            int kichThuoc,
            Long maHomeStay
    ) {
        Pageable pageable = PageRequest.of(
                trang,
                kichThuoc,
                Sort.by("id").descending()
        );

        if (maHomeStay == null) {
            throw new RuntimeException("Vui lòng chọn HomeStay!");
        }

        Page<GiaPhong> pageGiaPhong =
                giaPhongRepository
                        .findByPhong_HomeStay_IdAndPhong_TenPhongContainingIgnoreCase(
                                maHomeStay,
                                tuKhoa == null ? "" : tuKhoa.trim(),
                                pageable
                        );

        return pageGiaPhong.map(giaPhong -> {
            GiaPhongDTO dto = new GiaPhongDTO();

            dto.setId(giaPhong.getId());
            dto.setMaPhong(giaPhong.getPhong().getId());
            dto.setTenPhong(giaPhong.getPhong().getTenPhong());
            dto.setGiaPhong(giaPhong.getGiaPhong());
            dto.setNgayBatDau(giaPhong.getNgayBatDau());
            dto.setNgayKetThuc(giaPhong.getNgayKetThuc());

            return dto;
        });
    }

    @Override
    public void xoa(Long id) {
        GiaPhong giaPhong = giaPhongRepository.findById(id).orElseThrow();
        giaPhong.setTrangThai(TrangThai.DA_XOA);
        giaPhongRepository.save(giaPhong);
    }

    @Override
    public List<GiaPhongDTO> getGiaPhongByMaPhong(Long maPhong) {
        List<GiaPhong> danhSachGiaPhong = giaPhongRepository.findByPhong_IdOrderByNgayBatDauAsc(maPhong);
        return danhSachGiaPhong.stream().map(giaPhong -> {
            GiaPhongDTO dto = new GiaPhongDTO();

            dto.setId(giaPhong.getId());
            dto.setMaPhong(giaPhong.getPhong().getId());
            dto.setTenPhong(giaPhong.getPhong().getTenPhong());
            dto.setGiaPhong(giaPhong.getGiaPhong());
            dto.setNgayBatDau(giaPhong.getNgayBatDau());
            dto.setNgayKetThuc(giaPhong.getNgayKetThuc());

            return dto;
        }).toList();
    }
}
