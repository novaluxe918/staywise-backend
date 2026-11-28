package com.hoainhi.staywise.services.serviceimpl;

import com.hoainhi.staywise.dtos.HinhAnhHomeStayDTO;
import com.hoainhi.staywise.dtos.HomeStayDTO;
import com.hoainhi.staywise.dtos.HomeStayOptionDTO;
import com.hoainhi.staywise.entities.*;
import com.hoainhi.staywise.enums.TheLoaiHomeStay;
import com.hoainhi.staywise.enums.TrangThai;
import com.hoainhi.staywise.enums.TrangThaiHomeStay;
import com.hoainhi.staywise.enums.VaiTro;
import com.hoainhi.staywise.reponsitories.*;
import com.hoainhi.staywise.services.HomeStayService;
import com.hoainhi.staywise.services.cloudinary.CloudinaryService;
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
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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

    @Autowired
    private GiaPhongRepository giaPhongRepository;

    @Autowired
    private CloudinaryService cloudinaryService;


    @Override
    public Page<HomeStayDTO> showAll(
            String tuKhoa,
            int trang,
            int kichThuoc
    ) {
        Pageable pageable = PageRequest.of(
                trang,
                kichThuoc,
                Sort.by("id").descending()
        );

        Page<HomeStay> homeStays;

        if (tuKhoa == null || tuKhoa.trim().isEmpty()) {
            homeStays = homeStayRepository.findAll(pageable);
        } else {
            homeStays = homeStayRepository.findByTenHomeStayContaining(
                    tuKhoa.trim(),
                    pageable
            );
        }

        // 1. Lấy danh sách mã HomeStay trong trang hiện tại
        List<Long> danhSachMaHomeStay = homeStays.getContent()
                .stream()
                .map(HomeStay::getId)
                .toList();

        // 2. Truy vấn giá thấp nhất của từng HomeStay
        Map<Long, BigDecimal> mapGiaThapNhat = new HashMap<>();

        if (!danhSachMaHomeStay.isEmpty()) {
            List<Object[]> ketQuaGia =
                    giaPhongRepository.findGiaThapNhatTheoDanhSachHomeStay(
                            danhSachMaHomeStay,
                            TrangThai.DANG_HOAT_DONG,
                            LocalDateTime.now()
                    );

            mapGiaThapNhat = ketQuaGia.stream()
                    .collect(Collectors.toMap(
                            row -> (Long) row[0],
                            row -> (BigDecimal) row[1]
                    ));
        }

        // 3. Mapping dữ liệu HomeStay sang DTO
        Map<Long, BigDecimal> finalMapGiaThapNhat = mapGiaThapNhat;

        return homeStays.map(homeStay -> {

            HomeStayDTO dto = new HomeStayDTO();

            BeanUtils.copyProperties(homeStay, dto);

            // Thông tin chủ HomeStay
            if (homeStay.getNguoiDung() != null) {
                dto.setTenChuHomeStay(
                        homeStay.getNguoiDung().getHoTen()
                );
            }

            // Giá thấp nhất
            dto.setGiaThapNhat(
                    finalMapGiaThapNhat.get(homeStay.getId())
            );

            // Danh sách hình ảnh
            if (homeStay.getHomeStays() != null) {
                List<HinhAnhHomeStayDTO> danhSachAnh =
                        homeStay.getHomeStays()
                                .stream()
                                .map(anh -> {
                                    HinhAnhHomeStayDTO anhDTO =
                                            new HinhAnhHomeStayDTO();

                                    anhDTO.setId(anh.getId());
                                    anhDTO.setDuongDan(anh.getDuongDan());
                                    anhDTO.setLaAnhDaiDien(
                                            anh.isLaAnhDaiDien()
                                    );

                                    return anhDTO;
                                })
                                .toList();

                dto.setDanhSachHinhAnh(danhSachAnh);
            }

            // Danh sách tiện ích
            dto.setTienIchHienThi(
                    homeStay.getTienIchHomeStays()
                            .stream()
                            .filter(item -> item.getTienIch() != null)
                            .map(item -> item.getTienIch().getTenTienIch())
                            .toList()
            );

            // Danh sách dịch vụ
            dto.setDichVuHienThi(
                    homeStay.getHomeStayDichVus()
                            .stream()
                            .filter(item -> item.getDichVu() != null)
                            .map(item -> item.getDichVu().getTenDichVu())
                            .toList()
            );

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
    @Transactional
    public HomeStay tuChoiHomeStay(Long id, String lyDoTuChoi) {

        HomeStay homeStay = homeStayRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Không tìm thấy HomeStay")
                );

        if (homeStay.getTrangThaiHomeStay() != TrangThaiHomeStay.CHO_DUYET) {
            throw new RuntimeException(
                    "Chỉ được từ chối HomeStay đang chờ duyệt!"
            );
        }

        if (lyDoTuChoi == null || lyDoTuChoi.trim().isEmpty()) {
            throw new RuntimeException("Lý do từ chối không được để trống");
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


            if (dto.getTinhThanh() != null) {
                homeStay.setTinhThanh(dto.getTinhThanh());
            }

            if (dto.getEmail() != null) {
                homeStay.setEmail(dto.getEmail());
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

        if (Boolean.TRUE.equals(dto.getCapNhatTienIchHS())) {


            tienIchHomeStayRepository.deleteByHomeStay_Id(maHomeStay);


            List<Long> danhSachTienIch =
                    dto.getDanhSachTienIchHS() != null
                            ? dto.getDanhSachTienIchHS()
                            : List.of();

            for (Long item : danhSachTienIch) {

                TienIch tienIch = tienIchRepository.findById(item)
                        .orElseThrow(() ->
                                new RuntimeException("Không tìm thấy tiện ích " + item));

                TienIchHomeStay tienIchHomeStay = new TienIchHomeStay();
                tienIchHomeStay.setHomeStay(homeStay);
                tienIchHomeStay.setTienIch(tienIch);

                tienIchHomeStayRepository.save(tienIchHomeStay);
            }
        }

        if (Boolean.TRUE.equals(dto.getCapNhatDichVuHS())) {

            // Xóa toàn bộ dịch vụ cũ
            homeStayDichVuRepository.deleteByHomeStay_Id(maHomeStay);

            List<Long> danhSachDichVu =
                    dto.getDanhSachDichVuHS() != null
                            ? dto.getDanhSachDichVuHS()
                            : List.of();

            for (Long item : danhSachDichVu) {

                DichVu dichVu = dichVuRepository.findById(item)
                        .orElseThrow(() ->
                                new RuntimeException("Không tìm thấy dịch vụ " + item));

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


    @Override
    public List<HomeStayOptionDTO> layDanhSachHomeStayDaDuyet(String email) {

        NguoiDung nguoiDung = nguoiDungRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("Không tìm thấy tài khoản!")
                );

        return homeStayRepository.findHomeStayDaDuyet(
                nguoiDung.getId(),
                TrangThaiHomeStay.DA_DUYET
        );
    }

    @Override
    @Transactional
    public HomeStayDTO layThongTinHomeStay(Long id) {

        HomeStay homeStay = homeStayRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Không tìm thấy HomeStay!")
                );

        // Chỉ hiển thị HomeStay đã được duyệt
        if (homeStay.getTrangThaiHomeStay() != TrangThaiHomeStay.DA_DUYET) {
            throw new RuntimeException("HomeStay không tồn tại hoặc chưa được công khai!");
        }

        HomeStayDTO dto = new HomeStayDTO();

        BeanUtils.copyProperties(homeStay, dto);

        dto.setTenTheLoaiHomeStay(
                homeStay.getTheLoaiHomeStay() != null
                        ? homeStay.getTheLoaiHomeStay().getLabel()
                        : null
        );

        dto.setTienIchHienThi(
                homeStay.getTienIchHomeStays()
                        .stream()
                        .map(item -> item.getTienIch().getTenTienIch())
                        .toList()
        );

        dto.setDichVuHienThi(
                homeStay.getHomeStayDichVus()
                        .stream()
                        .map(item -> item.getDichVu().getTenDichVu())
                        .toList()
        );

        dto.setDanhSachHinhAnh(
                homeStay.getHomeStays()
                        .stream()
                        .map(anh -> {
                            HinhAnhHomeStayDTO anhDTO = new HinhAnhHomeStayDTO();

                            anhDTO.setId(anh.getId());
                            anhDTO.setDuongDan(anh.getDuongDan());
                            anhDTO.setLaAnhDaiDien(anh.isLaAnhDaiDien());

                            return anhDTO;
                        })
                        .toList()
        );

        return dto;
    }

    @Override
    public List<HomeStayDTO> layHomeStayTheoTheLoai(TheLoaiHomeStay theLoai) {

        Pageable pageable = PageRequest.of(0, 4);

        Page<HomeStay> page =
                homeStayRepository.findByTrangThaiHomeStayAndTheLoaiHomeStay(
                        TrangThaiHomeStay.DA_DUYET,
                        theLoai,
                        pageable
                );

        List<HomeStay> danhSachHomeStay = page.getContent();

        // Lấy danh sách mã HomeStay
        List<Long> danhSachMaHomeStay = danhSachHomeStay.stream()
                .map(HomeStay::getId)
                .toList();

        // Lấy giá thấp nhất đang có hiệu lực của các HomeStay
        Map<Long, BigDecimal> mapGiaThapNhat = new HashMap<>();

        if (!danhSachMaHomeStay.isEmpty()) {
            List<Object[]> ketQuaGia =
                    giaPhongRepository.findGiaThapNhatTheoDanhSachHomeStay(
                            danhSachMaHomeStay,
                            TrangThai.DANG_HOAT_DONG,
                            LocalDateTime.now()
                    );

            mapGiaThapNhat = ketQuaGia.stream()
                    .collect(Collectors.toMap(
                            row -> (Long) row[0],
                            row -> (BigDecimal) row[1]
                    ));
        }

        // Mapping HomeStayDTO
        Map<Long, BigDecimal> finalMapGiaThapNhat = mapGiaThapNhat;

        return danhSachHomeStay.stream()
                .map(hs -> {
                    HomeStayDTO dto = new HomeStayDTO();

                    dto.setId(hs.getId());
                    dto.setTenHomeStay(hs.getTenHomeStay());
                    dto.setMoTa(hs.getMoTa());
                    dto.setDiaChi(hs.getDiaChi());
                    dto.setPhuongXa(hs.getPhuongXa());
                    dto.setTinhThanh(hs.getTinhThanh());
                    dto.setTheLoaiHomeStay(hs.getTheLoaiHomeStay());

                    // Gán giá thấp nhất
                    dto.setGiaThapNhat(
                            finalMapGiaThapNhat.get(hs.getId())
                    );

                    // Map danh sách ảnh
                    List<HinhAnhHomeStayDTO> danhSachAnh =
                            hs.getHomeStays().stream()
                                    .map(anh -> {
                                        HinhAnhHomeStayDTO anhDTO =
                                                new HinhAnhHomeStayDTO();

                                        anhDTO.setId(anh.getId());
                                        anhDTO.setDuongDan(anh.getDuongDan());
                                        anhDTO.setLaAnhDaiDien(
                                                anh.isLaAnhDaiDien()
                                        );

                                        return anhDTO;
                                    })
                                    .toList();

                    dto.setDanhSachHinhAnh(danhSachAnh);

                    return dto;
                })
                .toList();
    }


}
