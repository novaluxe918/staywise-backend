package com.hoainhi.staywise.services.serviceimpl;

import com.hoainhi.staywise.dtos.TienIchDTO;
import com.hoainhi.staywise.entities.TheLoaiTienIch;
import com.hoainhi.staywise.entities.TienIch;
import com.hoainhi.staywise.enums.TrangThai;
import com.hoainhi.staywise.reponsitories.TheLoaiTienIchRepository;
import com.hoainhi.staywise.reponsitories.TienIchRepository;
import com.hoainhi.staywise.services.TienIchService;
import com.hoainhi.staywise.validations.CapNhat;
import com.hoainhi.staywise.validations.ThemMoi;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TienIchServiceImpl implements TienIchService {
    @Autowired
    private TienIchRepository tienIchRepository;

    @Autowired
    private Validator validator;

    @Autowired
    private TheLoaiTienIchRepository theLoaiTienIchRepository;
    @Override
    public TienIch themTienIch(TienIch tienIch, Long maTheLoai) {
        TheLoaiTienIch theLoai = theLoaiTienIchRepository
                .findById(maTheLoai)
                .orElseThrow(() ->
                        new RuntimeException("Không tìm thấy thể loại tiện ích"));

        tienIch.setTheLoaiTienIch(theLoai);
        tienIch.setNgayTao(LocalDateTime.now());
        return tienIchRepository.save(tienIch);
    }

    @Override
    public Page<TienIch> timKiemTienIch(String tuKhoa, int trang, int kichThuoc) {
        Pageable pageable = PageRequest.of(trang, kichThuoc, Sort.by("id").descending());
        if(tuKhoa == null || "".equals(tuKhoa.trim())){
            return tienIchRepository.findByTrangThaiNot(TrangThai.DA_XOA, pageable);
        }
        return tienIchRepository.findByTenTienIchContainingIgnoreCase(tuKhoa, pageable, TrangThai.DA_XOA);
    }

    @Override
    public void xoa(Long id) {
        TienIch tienIch = tienIchRepository.findById(id).orElseThrow();
        tienIch.setTrangThai(TrangThai.DA_XOA);
        tienIchRepository.save(tienIch);
    }

    @Override
    public Optional<TienIch> findById(Long id) {
        return tienIchRepository.findById(id);
    }

    @Override
    public String saveTienIch(TienIchDTO tienIchDTO) {
        boolean themMoi = tienIchDTO.getId() == null;
        Set<ConstraintViolation<TienIchDTO>> violations =
                themMoi
                        ? validator.validate(tienIchDTO, ThemMoi.class)
                        : validator.validate(tienIchDTO, CapNhat.class);

        if (!violations.isEmpty()) {
            String message = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining(", "));

            throw new ConstraintViolationException(message, violations);
        }
        TienIch tienIch;
        if(themMoi){
            tienIch = new TienIch();
            tienIch.setTrangThai(TrangThai.DANG_HOAT_DONG);
            tienIch.setNgayTao(LocalDateTime.now());
        }else {
            tienIch = tienIchRepository.findById(tienIchDTO.getId()).orElseThrow(
                    () -> new RuntimeException("Khong tim thay tien ich")
            );
        }

        if (tienIchDTO.getTenTienIch() != null) {
            tienIch.setTenTienIch(tienIch.getTenTienIch());
        }

        if (tienIchDTO.getMoTa() != null) {
            tienIch.setMoTa(tienIchDTO.getMoTa());
        }

        TheLoaiTienIch theLoai = theLoaiTienIchRepository
                .findById(tienIchDTO.getMatheloai())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Không tìm thấy thể loại tiện ích"
                        ));

        tienIch.setTheLoaiTienIch(theLoai);
        tienIchRepository.save(tienIch);
        return themMoi ? "Them thong tin thanh cong" : "Cap nhat thong tin thanh cong";
    }
}
