package com.hoainhi.staywise.services.serviceimpl;

import com.hoainhi.staywise.dtos.DuDoanGiaAIRequest;
import com.hoainhi.staywise.dtos.DuDoanGiaRequest;
import com.hoainhi.staywise.entities.DuDoanGia;
import com.hoainhi.staywise.response.DuDoanGiaAIResponse;
import com.hoainhi.staywise.response.DuDoanGiaResponse;
import com.hoainhi.staywise.entities.GiaPhong;
import com.hoainhi.staywise.entities.Phong;
import com.hoainhi.staywise.entities.SoNguoi;
import com.hoainhi.staywise.enums.TrangThai;
import com.hoainhi.staywise.reponsitories.DuDoanGiaRepository;
import com.hoainhi.staywise.reponsitories.GiaPhongRepository;
import com.hoainhi.staywise.reponsitories.PhongRepository;
import com.hoainhi.staywise.response.LichSuDuDoanGiaResponse;
import com.hoainhi.staywise.services.DuDoanGiaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class DuDoanGiaServiceImpl implements DuDoanGiaService {

    @Autowired
    private PhongRepository phongRepository;

    @Autowired
    private GiaPhongRepository giaPhongRepository;

    @Autowired
    private DuDoanGiaRepository duDoanGiaRepository;

    @Autowired
    private RestTemplate restTemplate;


    @Override
    public DuDoanGiaResponse duDoanGia(DuDoanGiaRequest request) {


        Phong phong = phongRepository.findById(request.getMaPhong())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phòng"));

        LocalDate ngayApDung = request.getNgayApDung();


        List<GiaPhong> danhSachGia = giaPhongRepository.findGiaPhongTheoNgay(
                phong.getId(),
                TrangThai.DANG_HOAT_DONG,
                ngayApDung.atStartOfDay(),
                ngayApDung.atTime(23, 59, 59)
        );

        if (danhSachGia.size() != 1) {
            throw new RuntimeException(
                    danhSachGia.isEmpty()
                            ? "Không tìm thấy giá phòng áp dụng"
                            : "Có nhiều mức giá cùng áp dụng"
            );
        }

        BigDecimal giaHienTai = danhSachGia.get(0).getGiaPhong();

        if (giaHienTai == null) {
            throw new RuntimeException("Giá phòng hiện tại không hợp lệ");
        }


        BigDecimal dienTich = phong.getDienTich();

        if (dienTich == null || dienTich.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Diện tích phòng không hợp lệ");
        }

        int soNguoi = phong.getSoNguois().stream()
                .mapToInt(SoNguoi::getSoLuong)
                .sum();

        if (soNguoi <= 0) {
            throw new RuntimeException("Số người tối đa không hợp lệ");
        }


        DuDoanGiaAIRequest aiRequest = DuDoanGiaAIRequest.builder()
                .maPhong(phong.getId())
                .ngayApDung(ngayApDung)
                .giaHienTai(giaHienTai.doubleValue())
                .dienTich(dienTich.doubleValue())
                .soNguoi(soNguoi)
                .loaiPhong(phong.getLoaiPhong().name())
                .tinhThanh(phong.getHomeStay().getTinhThanh())
                .build();

        DuDoanGiaAIResponse aiResponse = restTemplate.postForObject(
                "http://127.0.0.1:8000/predict",
                aiRequest,
                DuDoanGiaAIResponse.class
        );

        if (aiResponse == null || aiResponse.getGiaDuDoan() == null) {
            throw new RuntimeException("AI không trả về giá dự đoán");
        }


        LocalDateTime now = LocalDateTime.now();

        DuDoanGia duDoanGia = new DuDoanGia();
        duDoanGia.setPhong(phong);
        duDoanGia.setNgayDuDoan(now);
        duDoanGia.setGiaDuDoan(aiResponse.getGiaDuDoan());
        duDoanGia.setDoTinCay(aiResponse.getDoTinCay());
        duDoanGia.setNgayApDung(ngayApDung);
        duDoanGia.setPhienBan(aiResponse.getPhienBan());
        duDoanGia.setNgayTao(now);

        DuDoanGia daLuu = duDoanGiaRepository.save(duDoanGia);


        return DuDoanGiaResponse.builder()
                .id(daLuu.getId())
                .maPhong(phong.getId())
                .ngayApDung(ngayApDung)
                .giaHienTai(giaHienTai)
                .giaDuDoan(daLuu.getGiaDuDoan())
                .doTinCay(daLuu.getDoTinCay())
                .phienBan(daLuu.getPhienBan())
                .build();
    }



    @Override
    public List<LichSuDuDoanGiaResponse> xemLichSuDuDoanGia(Long maHomeStay) {

        List<DuDoanGia> danhSach = duDoanGiaRepository
                .findByPhong_HomeStay_IdOrderByNgayDuDoanDesc(maHomeStay);

        return danhSach.stream()
                .map(duDoan -> LichSuDuDoanGiaResponse.builder()
                        .id(duDoan.getId())
                        .maPhong(duDoan.getPhong().getId())
                        .tenPhong(duDoan.getPhong().getTenPhong())
                        .tenHomeStay(duDoan.getPhong()
                                .getHomeStay().getTenHomeStay())
                        .ngayDuDoan(duDoan.getNgayDuDoan())
                        .ngayApDung(duDoan.getNgayApDung())
                        .giaDuDoan(duDoan.getGiaDuDoan())
                        .doTinCay(duDoan.getDoTinCay())
                        .phienBan(duDoan.getPhienBan())
                        .build())
                .toList();
    }

    @Override
    public List<LichSuDuDoanGiaResponse> timKiemLichSuDuDoanGia(Long maPhong, LocalDate ngayApDung) {
        List<DuDoanGia> danhSach;
        if (maPhong != null && ngayApDung != null) {
            danhSach = duDoanGiaRepository
                    .findByPhong_IdAndNgayApDungOrderByNgayDuDoanDesc(
                            maPhong, ngayApDung);
        }

        // Trường hợp 2: Chỉ có mã phòng
        else if (maPhong != null) {
            danhSach = duDoanGiaRepository
                    .findByPhong_IdOrderByNgayDuDoanDesc(maPhong);
        }


        else if (ngayApDung != null) {
            danhSach = duDoanGiaRepository
                    .findByNgayApDungOrderByNgayDuDoanDesc(ngayApDung);
        }


        else {
            danhSach = duDoanGiaRepository
                    .findAllByOrderByNgayDuDoanDesc();
        }


        return danhSach.stream()
                .map(duDoan -> LichSuDuDoanGiaResponse.builder()
                        .id(duDoan.getId())
                        .maPhong(duDoan.getPhong().getId())
                        .tenPhong(duDoan.getPhong().getTenPhong())
                        .tenHomeStay(duDoan.getPhong()
                                .getHomeStay().getTenHomeStay())
                        .ngayDuDoan(duDoan.getNgayDuDoan())
                        .ngayApDung(duDoan.getNgayApDung())
                        .giaDuDoan(duDoan.getGiaDuDoan())
                        .doTinCay(duDoan.getDoTinCay())
                        .phienBan(duDoan.getPhienBan())
                        .build())
                .toList();
    }


}
