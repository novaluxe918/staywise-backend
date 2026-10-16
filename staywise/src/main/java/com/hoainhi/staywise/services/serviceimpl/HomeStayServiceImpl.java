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
    @Transactional
    public Long themHomeStay(HomeStayDTO homeStayDTO, Long maChuHomeStay) {

        HomeStay homeStay = new HomeStay();
        BeanUtils.copyProperties(homeStayDTO, homeStay);

        NguoiDung chuHomeStay = nguoiDungRepository
                .findByIdAndVaiTro(maChuHomeStay, VaiTro.OWNER)
                .orElseThrow(() ->
                        new RuntimeException("Không tìm thấy chủ HomeStay")
                );

        homeStay.setNguoiDung(chuHomeStay);
        homeStay.setTrangThaiHomeStay(TrangThaiHomeStay.CHO_DUYET);
        homeStay.setNgayDangKy(LocalDate.now());


        homeStayRepository.save(homeStay);

        // Thêm tiện ích
        if (homeStayDTO.getDanhSachTienIchHS() != null) {

            for (Long item : homeStayDTO.getDanhSachTienIchHS()) {

                TienIch tienIch = tienIchRepository.findById(item)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Không tìm thấy tiện ích ID: " + item
                                )
                        );

                TienIchHomeStay tienIchHomeStay = new TienIchHomeStay();
                tienIchHomeStay.setHomeStay(homeStay);
                tienIchHomeStay.setTienIch(tienIch);

                tienIchHomeStayRepository.save(tienIchHomeStay);
            }
        }

        // Thêm dịch vụ
        if (homeStayDTO.getDanhSachDichVuHS() != null) {

            for (Long item : homeStayDTO.getDanhSachDichVuHS()) {

                DichVu dichVu = dichVuRepository.findById(item)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Không tìm thấy dịch vụ ID: " + item
                                )
                        );

                HomeStayDichVu stayDichVu = new HomeStayDichVu();
                stayDichVu.setHomeStay(homeStay);
                stayDichVu.setDichVu(dichVu);

                homeStayDichVuRepository.save(stayDichVu);
            }
        }

        return homeStay.getId();
    }


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
    public Long capNhatHomeStay(Long id, HomeStayDTO dto) {

        HomeStay homeStay = homeStayRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Không tìm thấy HomeStay!"));

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

        if (dto.getEmail() != null) {
            homeStay.setEmail(dto.getEmail());
        }

        if (dto.getTheLoaiHomeStay() != null) {
            homeStay.setTheLoaiHomeStay(dto.getTheLoaiHomeStay());
        }

        if (dto.getDanhSachTienIchHS() != null) {
            homeStay.getTienIchHomeStays().clear();

            for(Long item : dto.getDanhSachTienIchHS()){
                TienIch tienIch = tienIchRepository.findById(item).orElseThrow(() -> new RuntimeException("Khong tim thay"));
                TienIchHomeStay tienIchHomeStay = new TienIchHomeStay();
                tienIchHomeStay.setHomeStay(homeStay);
                tienIchHomeStay.setTienIch(tienIch);
               homeStay.getTienIchHomeStays().add(tienIchHomeStay);
            }
        }
        if (dto.getDanhSachDichVuHS() != null) {
            homeStay.getHomeStayDichVus().clear();
            for(Long item : dto.getDanhSachDichVuHS()){
                DichVu dichVu = dichVuRepository.findById(item).orElseThrow(() -> new RuntimeException("Khong tim thay") );
                HomeStayDichVu stayDichVu = new HomeStayDichVu();
                stayDichVu.setHomeStay(homeStay);
                stayDichVu.setDichVu(dichVu);
                homeStay.getHomeStayDichVus().add(stayDichVu);
            }
        }

        homeStayRepository.save(homeStay);

        return homeStay.getId();
    }

    @Override
    @Transactional
    public String saveHomeStay(
            HomeStayDTO dto,
            List<MultipartFile> files) throws IOException {

        Long maHomeStay;

        if (dto.getId() == null) {
            // kiểm tra validation
            Set<ConstraintViolation<HomeStayDTO>> violations =
                    validator.validate(dto);

            if (!violations.isEmpty()) {
                throw new ConstraintViolationException(violations);
            }

            maHomeStay = themHomeStay(dto, dto.getMaChuHomeStay());


        } else {

            maHomeStay = capNhatHomeStay(dto.getId(), dto);
        }

        // Lưu hình ảnh nếu có
        if (files != null && !files.isEmpty()) {
            hinhAnhHomeStaySevice.themAnh(files, maHomeStay);
        }

        if (dto.getId() == null) {
            return "Đăng ký HomeStay thành công, đang chờ Admin phê duyệt!";
        }

        return "Cập nhật thông tin HomeStay thành công!";
    }

}
