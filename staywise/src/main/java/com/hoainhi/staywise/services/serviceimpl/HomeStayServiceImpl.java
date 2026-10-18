package com.hoainhi.staywise.services.serviceimpl;

import com.hoainhi.staywise.dtos.HomeStayDTO;
import com.hoainhi.staywise.entities.*;
import com.hoainhi.staywise.enums.TrangThaiHomeStay;
import com.hoainhi.staywise.enums.VaiTro;
import com.hoainhi.staywise.reponsitories.*;
import com.hoainhi.staywise.services.HomeStayService;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Service
public class HomeStayServiceImpl implements HomeStayService {

    @Autowired
    private HomeStayRepository homeStayRepository;

    @Autowired
    private NguoiDungRepository nguoiDungRepository;

    @Autowired
    private TienIchRepository tienIchRepository;

    @Autowired
    private DichVuRepository dichVuRepository;
    @Autowired
    private TienIchHomeServiceImpl tienIchHomeService;

    @Autowired
    private HomeStayDichVuImpl homeStayDichVu;

    @Autowired
    private HinhAnhHomeStaySeviceImpl hinhAnhHomeStaySevice;

    @Autowired
    private Validator validator;

    @Autowired
    private TienIchHomeStayRepository tienIchHomeStayRepository;

    @Autowired
    private HomeStayDichVuRepository homeStayDichVuRepository;


    @Override
    public Page<HomeStayDTO> showAll(String tuKhoa, int trang, int kichThuoc) {
        Pageable pageable = PageRequest.of(trang, kichThuoc, Sort.by("id").descending());
        Page<HomeStay> homeStays =
                homeStayRepository.findByTenHomeStayContaining(tuKhoa, pageable);

        return homeStays.map(homeStay -> {
            HomeStayDTO dto = new HomeStayDTO();

            BeanUtils.copyProperties(homeStay, dto);

            return dto;
        });
    }

    @Override
    public HomeStay getById(Long id) {
        HomeStay homeStay = homeStayRepository.findById(id).orElseThrow(() -> new RuntimeException("Khong tim thay homestay"));
        if(homeStay.getTrangThaiHomeStay() == TrangThaiHomeStay.DA_XOA){
            throw new RuntimeException("Homestay da bi xoa");
        }
        return homeStay;
    }

    @Override
    public HomeStay duyetHomeStay(Long id) {
        HomeStay homeStay = homeStayRepository.findById(id).orElseThrow(() -> new RuntimeException("Khong tim thay homestay"));
        if(homeStay.getTrangThaiHomeStay() != TrangThaiHomeStay.CHO_DUYET){
             throw new RuntimeException("HomeStay khong o trang thai cho duyet");

        }
        homeStay.setNgayPheDuyet(LocalDate.now());
        homeStay.setTrangThaiHomeStay(TrangThaiHomeStay.DA_DUYET);
        return homeStayRepository.save(homeStay);
    }

    @Override
    public HomeStay tuChoiHomeStay(Long id, String lyDoTuChoi) {
        HomeStay homeStay = homeStayRepository.findById(id).orElseThrow(() -> new RuntimeException("Khong tim thay homestay"));
        if (homeStay.getTrangThaiHomeStay() != TrangThaiHomeStay.CHO_DUYET) {
            throw new RuntimeException(
                    "Chỉ được từ chối HomeStay đang chờ duyệt!");
        }
        homeStay.setTrangThaiHomeStay(TrangThaiHomeStay.TU_CHOI);
        homeStay.setLyDoTuChoi(lyDoTuChoi.trim());
        return homeStayRepository.save(homeStay);
    }

    @Override
    public void xoa(Long id) {
        HomeStay homeStay = homeStayRepository.findById(id).orElseThrow(() -> new RuntimeException("Khong tim thay HomeStay"));
        homeStay.setTrangThaiHomeStay(TrangThaiHomeStay.DA_XOA);
        homeStayRepository.save(homeStay);
    }

    @Override
    public void ngungKinhDoanh(Long id) {
        HomeStay homeStay = homeStayRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Không tìm thấy HomeStay")
                );

        homeStay.setTrangThaiHomeStay(
                TrangThaiHomeStay.TAM_NGUNG
        );

        homeStayRepository.save(homeStay);
    }


    @Override
    @Transactional
    public String saveHomeStay(
            HomeStayDTO dto,
            List<MultipartFile> files, Long maChuHomeStay) throws IOException {
         boolean themMoi = dto.getId() == null;
        NguoiDung nguoiDung = nguoiDungRepository.findByIdAndVaiTro(maChuHomeStay, VaiTro.OWNER).orElseThrow(
                () -> new RuntimeException("Khong tim thay chu HomeStay")
        );
        HomeStay homeStay;
        if (themMoi) {
            // 1. Thêm mới HomeStay
            Set<ConstraintViolation<HomeStayDTO>> violations =
                    validator.validate(dto);

            if (!violations.isEmpty()) {
                throw new ConstraintViolationException(violations);
            }

            homeStay = new HomeStay();

            BeanUtils.copyProperties(dto, homeStay);

            homeStay.setNguoiDung(nguoiDung);
            homeStay.setTrangThaiHomeStay(TrangThaiHomeStay.CHO_DUYET);
            homeStay.setNgayDangKy(LocalDate.now());

        } else {
            // 2. Tìm HomeStay cần cập nhật
            homeStay = homeStayRepository.findById(dto.getId())
                    .orElseThrow(() ->
                            new RuntimeException("Không tìm thấy HomeStay!"));

            // Kiểm tra đúng chủ sở hữu
            if (!homeStay.getNguoiDung().getId().equals(maChuHomeStay)) {
                throw new RuntimeException(
                        "Bạn không có quyền cập nhật HomeStay này!");
            }

            // Cập nhật thông tin
            if (dto.getTenHomeStay() != null) {
                homeStay.setTenHomeStay(dto.getTenHomeStay());
            }

            if (dto.getMoTa() != null) {
                homeStay.setMoTa(dto.getMoTa());
            }

            if (dto.getPhuongXa() != null) {
                homeStay.setPhuongXa(dto.getPhuongXa());
            }

            if (dto.getQuanHuyen() != null) {
                homeStay.setQuanHuyen(dto.getQuanHuyen());
            }

            if (dto.getTinhThanh() != null) {
                homeStay.setTinhThanh(dto.getTinhThanh());
            }

            if (dto.getDiaChi() != null) {
                homeStay.setDiaChi(dto.getDiaChi());
            }

            if (dto.getSoDienThoai() != null) {
                homeStay.setSoDienThoai(dto.getSoDienThoai());
            }
        }

        // 3. Lưu HomeStay
        homeStayRepository.save(homeStay);

        Long maHomeStay = homeStay.getId();

        if (dto.getDanhSachTienIchHS() != null) {

            // Xóa các tiện ích liên kết cũ
            tienIchHomeStayRepository.deleteByHomeStay_Id(maHomeStay);

            for (Long item : dto.getDanhSachTienIchHS()) {

                TienIch tienIch = tienIchRepository.findById(item)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Không tìm thấy tiện ích " + item));

                TienIchHomeStay tienIchHomeStay = new TienIchHomeStay();
                tienIchHomeStay.setHomeStay(homeStay);
                tienIchHomeStay.setTienIch(tienIch);

                tienIchHomeStayRepository.save(tienIchHomeStay);
            }
        }

        // Xử lý dịch vụ
        if (dto.getDanhSachDichVuHS() != null) {

            homeStayDichVuRepository.deleteByHomeStay_Id(maHomeStay);

            for (Long item : dto.getDanhSachDichVuHS()) {

                DichVu dichVu = dichVuRepository.findById(item)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Không tìm thấy dịch vụ  " + item));

                HomeStayDichVu stayDichVu = new HomeStayDichVu();
                stayDichVu.setHomeStay(homeStay);
                stayDichVu.setDichVu(dichVu);

                homeStayDichVuRepository.save(stayDichVu);
            }
        }

        // Lưu hình ảnh
        if (files != null && !files.isEmpty()) {
            hinhAnhHomeStaySevice.themAnh(files, maHomeStay);
        }


        if (dto.getId() == null) {
            return "Đăng ký HomeStay thành công, đang chờ Admin phê duyệt!";
        }

        return "Cập nhật thông tin HomeStay thành công!";
    }

}
