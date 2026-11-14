package com.hoainhi.staywise.services.serviceimpl;

import com.hoainhi.staywise.dtos.CapNhatTrangThaiXacMinhDTO;
import com.hoainhi.staywise.dtos.NguoiDungDTO;
import com.hoainhi.staywise.dtos.NguoiDungResponseDTO;
import com.hoainhi.staywise.entities.HoSoChuHomeStay;
import com.hoainhi.staywise.entities.NguoiDung;
import com.hoainhi.staywise.enums.TrangThaiXacMinh;
import com.hoainhi.staywise.enums.VaiTro;
import com.hoainhi.staywise.reponsitories.HoSoChuHomeStayRepository;
import com.hoainhi.staywise.reponsitories.NguoiDungRepository;
import com.hoainhi.staywise.services.NguoiDungService;
import com.hoainhi.staywise.services.cloudinary.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class NguoiDungServiceImpl implements NguoiDungService {
    @Autowired
    private NguoiDungRepository nguoiDungRepository;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private HoSoChuHomeStayRepository hoSoChuHomeStayRepository;


    @Override
    @Transactional
    public void capNhatThongTin(String email, NguoiDungDTO dto) throws IOException {

        // 1. Tìm tài khoản theo email
        NguoiDung nd = nguoiDungRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        // 2. Cập nhật thông tin cá nhân trong bảng nguoidung
        nd.setHoTen(dto.getHoTen());

        nd.setSoDienThoai(
                dto.getSoDienThoai() == null || dto.getSoDienThoai().isBlank()
                        ? null
                        : dto.getSoDienThoai()
        );

        // 3. Cập nhật ảnh đại diện
        if (dto.getAnhDaiDien() != null && !dto.getAnhDaiDien().isEmpty()) {
            nd.setAnhDaiDien(
                    cloudinaryService.taiAnh(dto.getAnhDaiDien())
            );
        }

        // 4. Kiểm tra có ảnh giấy tờ nào được gửi lên không
        boolean thayDoiGiayTo =
                (dto.getAnhCCCDMatTruoc() != null && !dto.getAnhCCCDMatTruoc().isEmpty())
                        || (dto.getAnhCCCDMatSau() != null && !dto.getAnhCCCDMatSau().isEmpty())
                        || (dto.getAnhGiayPhepKinhDoanh() != null && !dto.getAnhGiayPhepKinhDoanh().isEmpty());

        // 5. Nếu có thay đổi giấy tờ thì cập nhật bảng hồ sơ
        if (thayDoiGiayTo) {

            if (nd.getVaiTro() != VaiTro.OWNER) {
                throw new RuntimeException("Chỉ chủ HomeStay mới được cập nhật giấy tờ xác minh");
            }

            // Tìm hồ sơ hiện tại
            HoSoChuHomeStay hoSo = hoSoChuHomeStayRepository
                    .findByNguoiDung_Id(nd.getId())
                    .orElseGet(() -> {
                        // Nếu chưa có hồ sơ thì tạo mới
                        HoSoChuHomeStay hs = new HoSoChuHomeStay();
                        hs.setNguoiDung(nd);
                        return hs;
                    });

            // CCCD mặt trước
            if (dto.getAnhCCCDMatTruoc() != null
                    && !dto.getAnhCCCDMatTruoc().isEmpty()) {

                hoSo.setAnhCCCDMatTruoc(
                        cloudinaryService.taiAnh(dto.getAnhCCCDMatTruoc())
                );
            }

            // CCCD mặt sau
            if (dto.getAnhCCCDMatSau() != null
                    && !dto.getAnhCCCDMatSau().isEmpty()) {

                hoSo.setAnhCCCDMatSau(
                        cloudinaryService.taiAnh(dto.getAnhCCCDMatSau())
                );
            }

            // Giấy phép kinh doanh
            if (dto.getAnhGiayPhepKinhDoanh() != null
                    && !dto.getAnhGiayPhepKinhDoanh().isEmpty()) {

                hoSo.setAnhGiayPhepKinhDoanh(
                        cloudinaryService.taiAnh(dto.getAnhGiayPhepKinhDoanh())
                );
            }

            hoSo.setTrangThaiXacMinh(TrangThaiXacMinh.CHUA_XAC_MINH);
            hoSo.setNgayXacMinh(null);
            hoSo.setLyDoTuChoiXacMinh(null);

            hoSoChuHomeStayRepository.save(hoSo);
        }


        nguoiDungRepository.save(nd);
    }



    @Override
    public NguoiDungResponseDTO layThongTin(String email) {

        NguoiDung nd = nguoiDungRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        NguoiDungResponseDTO dto = new NguoiDungResponseDTO();

        dto.setId(nd.getId());
        dto.setHoTen(nd.getHoTen());
        dto.setEmail(nd.getEmail());
        dto.setSoDienThoai(nd.getSoDienThoai());
        dto.setAnhDaiDien(nd.getAnhDaiDien());


        if (nd.getVaiTro() == VaiTro.OWNER) {

            HoSoChuHomeStay hoSo = hoSoChuHomeStayRepository
                    .findByNguoiDung_Id(nd.getId())
                    .orElse(null);

            if (hoSo != null) {

                dto.setAnhCCCDMatTruoc(
                        hoSo.getAnhCCCDMatTruoc()
                );

                dto.setAnhCCCDMatSau(
                        hoSo.getAnhCCCDMatSau()
                );

                dto.setAnhGiayPhepKinhDoanh(
                        hoSo.getAnhGiayPhepKinhDoanh()
                );

                dto.setTrangThaiXacMinh(
                        hoSo.getTrangThaiXacMinh()
                );

                dto.setLyDoTuChoiXacMinh(
                        hoSo.getLyDoTuChoiXacMinh()
                );
            }
        }

        return dto;
    }


    @Override
    public void capNhatAnhDaiDien(String email, MultipartFile anhDaiDien) throws IOException {
        NguoiDung nd = nguoiDungRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("Không tìm thấy người dùng"));

        if (anhDaiDien == null || anhDaiDien.isEmpty()) {
            throw new RuntimeException("Vui lòng chọn ảnh đại diện");
        }

        String urlAnh = cloudinaryService.taiAnh(anhDaiDien);

        nd.setAnhDaiDien(urlAnh);

        nguoiDungRepository.save(nd);
    }


    @Override
    public List<NguoiDungResponseDTO> getDanhSachOwner() {

        List<NguoiDung> owners =
                nguoiDungRepository.findByVaiTro(VaiTro.OWNER);

        return owners.stream().map(owner -> {

            NguoiDungResponseDTO response = new NguoiDungResponseDTO();


            response.setId(owner.getId());
            response.setHoTen(owner.getHoTen());
            response.setEmail(owner.getEmail());
            response.setSoDienThoai(owner.getSoDienThoai());
            response.setAnhDaiDien(owner.getAnhDaiDien());

            HoSoChuHomeStay hoSo = hoSoChuHomeStayRepository
                    .findByNguoiDung_Id(owner.getId())
                    .orElse(null);


            if (hoSo != null) {
                response.setAnhCCCDMatTruoc(
                        hoSo.getAnhCCCDMatTruoc()
                );

                response.setAnhCCCDMatSau(
                        hoSo.getAnhCCCDMatSau()
                );

                response.setAnhGiayPhepKinhDoanh(
                        hoSo.getAnhGiayPhepKinhDoanh()
                );

                response.setTrangThaiXacMinh(
                        hoSo.getTrangThaiXacMinh()
                );

                response.setLyDoTuChoiXacMinh(
                        hoSo.getLyDoTuChoiXacMinh()
                );
            }

            return response;

        }).toList();
    }

    @Override
    @Transactional
    public void capNhatTrangThaiXacMinh(
            Long maNguoiDung,
            CapNhatTrangThaiXacMinhDTO dto) {

        HoSoChuHomeStay hoSo = hoSoChuHomeStayRepository
                .findByNguoiDung_Id(maNguoiDung)
                .orElseThrow(() ->
                        new RuntimeException("Không tìm thấy hồ sơ chủ HomeStay"));

        if (dto.getTrangThaiXacMinh() == null) {
            throw new RuntimeException("Vui lòng chọn trạng thái xác minh");
        }

        if (dto.getTrangThaiXacMinh() == TrangThaiXacMinh.TU_CHOI) {
            if (dto.getLyDoTuChoiXacMinh() == null
                    || dto.getLyDoTuChoiXacMinh().isBlank()) {
                throw new RuntimeException("Vui lòng nhập lý do từ chối");
            }

            hoSo.setLyDoTuChoiXacMinh(dto.getLyDoTuChoiXacMinh().trim());
        } else {
            hoSo.setLyDoTuChoiXacMinh(null);
        }

        hoSo.setTrangThaiXacMinh(dto.getTrangThaiXacMinh());
        hoSo.setNgayXacMinh(LocalDateTime.now());

        hoSoChuHomeStayRepository.save(hoSo);
    }

}
