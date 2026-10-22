package com.hoainhi.staywise.services.serviceimpl;

import com.hoainhi.staywise.dtos.ChiTietGiuongDTO;
import com.hoainhi.staywise.dtos.PhongDTO;
import com.hoainhi.staywise.dtos.SoNguoiDTO;
import com.hoainhi.staywise.entities.*;
import com.hoainhi.staywise.enums.TrangThaiHomeStay;
import com.hoainhi.staywise.enums.TrangThaiPhong;
import com.hoainhi.staywise.reponsitories.*;
import com.hoainhi.staywise.services.PhongService;
import com.hoainhi.staywise.validations.CapNhat;
import com.hoainhi.staywise.validations.ThemMoi;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PhongServiceImpl implements PhongService {
    @Autowired
    private PhongRepository phongRepository;

    @Autowired
    private HomeStayRepository homeStayRepository;

    @Autowired
    private LoaiGiuongRepository loaiGiuongRepository;

    @Autowired
    private ChiTietGiuongRepository chiTietGiuongRepository;

    @Autowired
    private SoNguoiRepository soNguoiRepository;

    @Autowired
    private Validator validator;

    @Override
    public Page<PhongDTO> showAll(String tuKhoa, int trang, int kichThuoc) {

        Pageable pageable = PageRequest.of(
                trang,
                kichThuoc,
                Sort.by("id").descending()
        );

        Page<Phong> pagePhong;

        if (tuKhoa == null || tuKhoa.trim().isEmpty()) {
            pagePhong = phongRepository.findByTrangThaiPhongNot(
                    TrangThaiPhong.DA_XOA,
                    pageable
            );
        } else {
            pagePhong = phongRepository.findByTenPhongContainingIgnoreCase(
                    tuKhoa.trim(),
                    TrangThaiPhong.DA_XOA,
                    pageable
            );
        }

        return pagePhong.map(phong -> {
            PhongDTO dto = new PhongDTO();

            BeanUtils.copyProperties(
                    phong,
                    dto,
                    "homeStay"
            );

            if (phong.getHomeStay() != null) {
                dto.setMaHomeStay(
                        phong.getHomeStay().getId()
                );
            }

            return dto;
        });
    }

    @Override

    public void xoa(Long id) {
        Phong phong = phongRepository.findById(id).orElseThrow(() -> new RuntimeException("Khong tim thay phong"));
        phong.setTrangThaiPhong(TrangThaiPhong.DA_XOA);
        phongRepository.save(phong);
    }



    @Override
    @Transactional
    public String savePhong(PhongDTO phongDTO) {
         boolean themMoi = phongDTO.getId() == null;
        Set<ConstraintViolation<PhongDTO>> violations =
                themMoi
                        ? validator.validate(phongDTO, ThemMoi.class)
                        : validator.validate(phongDTO, CapNhat.class);

        if (!violations.isEmpty()) {
            String message = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining(", "));

            throw new ConstraintViolationException(message, violations);
        }

        Phong phong;
         if(themMoi){
             phong = new Phong();
             HomeStay homeStay = homeStayRepository.findById(phongDTO.getMaHomeStay()).orElseThrow(
                     () -> new RuntimeException("Khong tim thay homestay")
             );

             if(homeStay.getTrangThaiHomeStay() != TrangThaiHomeStay.DA_DUYET){
                 throw new RuntimeException(
                         "HomeStay chưa được duyệt hoặc không còn hoạt động!");

             }
             phong.setHomeStay(homeStay);
             phong.setTrangThaiPhong(TrangThaiPhong.HOAT_DONG);
             phong.setNgayTao(LocalDateTime.now());

         } else {
             phong = phongRepository.findById(phongDTO.getId()).orElseThrow(() -> new RuntimeException("Khong tim thay phong"));
         }

        BeanUtils.copyProperties(
                phongDTO,
                phong,
                "id",
                "maHomeStay",
                "trangThaiPhong",
                "ngayTao",
                "danhSachGiuong"
        );
        if (phongDTO.getDanhSachGiuong() != null) {
            phong.getChiTietGiuongs().clear();

            for (ChiTietGiuongDTO item : phongDTO.getDanhSachGiuong()) {

                LoaiGiuong loaiGiuong = loaiGiuongRepository
                        .findById(item.getMaLoaiGiuong())
                        .orElseThrow(() ->
                                new RuntimeException("Không tìm thấy loại giường"));

                ChiTietGiuong chiTiet = new ChiTietGiuong();
                chiTiet.setPhong(phong);
                chiTiet.setLoaiGiuong(loaiGiuong);
                chiTiet.setSoLuong(item.getSoLuong());

                phong.getChiTietGiuongs().add(chiTiet);
            }
        }
        phongRepository.save(phong);
        if(phongDTO.getDanhSachSoNguoi() != null){
            List<SoNguoi> danhSach = soNguoiRepository.findByPhong_Id(phong.getId());
            soNguoiRepository.deleteAll(danhSach);
            for(SoNguoiDTO item : phongDTO.getDanhSachSoNguoi()){
                SoNguoi soNguoi = new SoNguoi();
                soNguoi.setPhong(phong);
                soNguoi.setSoLuong(item.getSoLuong());
                soNguoi.setTheLoaiNguoi(item.getTheLoaiNguoi());
                soNguoiRepository.save(soNguoi);

            }
        }
         return themMoi ? "Them thong tin thanh cong" : "Cap nhat thong tin thanh cong";
    }



}
