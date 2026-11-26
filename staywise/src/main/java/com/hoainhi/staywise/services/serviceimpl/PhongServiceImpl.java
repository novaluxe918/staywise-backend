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
    public Page<PhongDTO> showAll(
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

        Page<Phong> pagePhong;

        boolean coTuKhoa = tuKhoa != null && !tuKhoa.trim().isEmpty();

        if (maHomeStay != null) {

            if (coTuKhoa) {
                pagePhong =
                        phongRepository
                                .findByHomeStay_IdAndTenPhongContainingIgnoreCaseAndTrangThaiPhongNot(
                                        maHomeStay,
                                        tuKhoa.trim(),
                                        TrangThaiPhong.DA_XOA,
                                        pageable
                                );
            } else {
                pagePhong =
                        phongRepository
                                .findByHomeStay_IdAndTrangThaiPhongNot(
                                        maHomeStay,
                                        TrangThaiPhong.DA_XOA,
                                        pageable
                                );
            }

        } else {

            if (coTuKhoa) {
                pagePhong = phongRepository.findByTenPhongContainingIgnoreCase(
                        tuKhoa.trim(),
                        TrangThaiPhong.DA_XOA,
                        pageable
                );
            } else {
                pagePhong = phongRepository.findByTrangThaiPhongNot(
                        TrangThaiPhong.DA_XOA,
                        pageable
                );
            }
        }

        return pagePhong.map(phong -> {

            // 1. Khởi tạo DTO
            PhongDTO dto = new PhongDTO();

            // 2. Mapping thông tin cơ bản của phòng
            dto.setId(phong.getId());

            dto.setMaHomeStay(
                    phong.getHomeStay() != null
                            ? phong.getHomeStay().getId()
                            : null
            );

            dto.setTenPhong(phong.getTenPhong());
            dto.setMoTa(phong.getMoTa());
            dto.setDienTich(phong.getDienTich());
            dto.setLoaiPhong(phong.getLoaiPhong());
            dto.setTrangThaiPhong(phong.getTrangThaiPhong());

            // 3. Mapping danh sách giường
            dto.setDanhSachGiuong(
                    phong.getChiTietGiuongs().stream()
                            .map(chiTiet -> {

                                ChiTietGiuongDTO giuongDTO =
                                        new ChiTietGiuongDTO();

                                giuongDTO.setMaLoaiGiuong(
                                        chiTiet.getLoaiGiuong().getId()
                                );

                                giuongDTO.setTenGiuong(
                                        chiTiet.getLoaiGiuong().getTenGiuong()
                                );

                                giuongDTO.setSoLuong(
                                        chiTiet.getSoLuong()
                                );

                                return giuongDTO;
                            })
                            .toList()
            );

            // 4. Mapping danh sách sức chứa
            dto.setDanhSachSoNguoi(
                    phong.getSoNguois().stream()
                            .map(soNguoi -> {

                                SoNguoiDTO soNguoiDTO = new SoNguoiDTO();

                                soNguoiDTO.setId(soNguoi.getId());
                                soNguoiDTO.setSoLuong(soNguoi.getSoLuong());
                                soNguoiDTO.setTheLoaiNguoi(
                                        soNguoi.getTheLoaiNguoi()
                                );

                                return soNguoiDTO;
                            })
                            .toList()
            );

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

        //validation
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

        if (themMoi) {
            phong = new Phong();

            HomeStay homeStay = homeStayRepository
                    .findById(phongDTO.getMaHomeStay())
                    .orElseThrow(() ->
                            new RuntimeException("Không tìm thấy homestay"));

            if (homeStay.getTrangThaiHomeStay()
                    != TrangThaiHomeStay.DA_DUYET) {
                throw new RuntimeException(
                        "HomeStay chưa được duyệt hoặc không còn hoạt động!");
            }

            phong.setHomeStay(homeStay);
            phong.setTrangThaiPhong(TrangThaiPhong.HOAT_DONG);
            phong.setNgayTao(LocalDateTime.now());


        } else {
            phong = phongRepository.findById(phongDTO.getId())
                    .orElseThrow(() ->
                            new RuntimeException("Không tìm thấy phòng"));
        }

        if (phongDTO.getTenPhong() != null) {
            phong.setTenPhong(phongDTO.getTenPhong());
        }

        if (phongDTO.getMoTa() != null) {
            phong.setMoTa(phongDTO.getMoTa());
        }

        if (phongDTO.getDienTich() != null) {
            phong.setDienTich(phongDTO.getDienTich());
        }

        if (phongDTO.getLoaiPhong() != null) {
            phong.setLoaiPhong(phongDTO.getLoaiPhong());
        }

        if (phongDTO.getDanhSachGiuong() != null) {

            if (phong.getChiTietGiuongs() == null) {
                phong.setChiTietGiuongs(new ArrayList<>());
            }

            phong.getChiTietGiuongs().clear();

            for (ChiTietGiuongDTO item :
                    phongDTO.getDanhSachGiuong()) {

                LoaiGiuong loaiGiuong = loaiGiuongRepository
                        .findById(item.getMaLoaiGiuong())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Không tìm thấy loại giường"));

                ChiTietGiuong chiTiet = new ChiTietGiuong();
                chiTiet.setPhong(phong);
                chiTiet.setLoaiGiuong(loaiGiuong);
                chiTiet.setSoLuong(item.getSoLuong());

                phong.getChiTietGiuongs().add(chiTiet);
            }
        }


        phong = phongRepository.saveAndFlush(phong);


        if (phongDTO.getDanhSachSoNguoi() != null) {

            List<SoNguoi> danhSachCu =
                    soNguoiRepository.findByPhong_Id(phong.getId());

            soNguoiRepository.deleteAll(danhSachCu);

            for (SoNguoiDTO item :
                    phongDTO.getDanhSachSoNguoi()) {

                SoNguoi soNguoi = new SoNguoi();
                soNguoi.setPhong(phong);
                soNguoi.setSoLuong(item.getSoLuong());
                soNguoi.setTheLoaiNguoi(item.getTheLoaiNguoi());

                soNguoiRepository.save(soNguoi);
            }
        }

        return themMoi
                ? "Thêm thông tin phòng thành công"
                : "Cập nhật thông tin phòng thành công";
    }




}
