package com.hoainhi.staywise.services.serviceimpl;

import com.hoainhi.staywise.dtos.ChiTietGiuongDTO;
import com.hoainhi.staywise.dtos.PhongDTO;
import com.hoainhi.staywise.entities.ChiTietGiuong;
import com.hoainhi.staywise.entities.HomeStay;
import com.hoainhi.staywise.entities.LoaiGiuong;
import com.hoainhi.staywise.entities.Phong;
import com.hoainhi.staywise.enums.TrangThaiHomeStay;
import com.hoainhi.staywise.enums.TrangThaiPhong;
import com.hoainhi.staywise.reponsitories.ChiTietGiuongRepository;
import com.hoainhi.staywise.reponsitories.HomeStayRepository;
import com.hoainhi.staywise.reponsitories.LoaiGiuongRepository;
import com.hoainhi.staywise.reponsitories.PhongRepository;
import com.hoainhi.staywise.services.PhongService;
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

    @Override
    @Transactional
    public Phong themPhong(PhongDTO dto) {
        HomeStay homeStay = homeStayRepository.findById(dto.getMaHomeStay()).orElseThrow(() -> new RuntimeException("Khong tim thay HomeStay"));
        if(homeStay.getTrangThaiHomeStay() != TrangThaiHomeStay.DA_DUYET){
            throw  new RuntimeException("HomeStay chưa được duyệt hoặc không còn hoạt động. Không thể thêm phòng!");
        }

        Phong phong = new Phong();
        BeanUtils.copyProperties(dto, phong, "id", "maHomeStay");
        phong.setHomeStay(homeStay);
        phong.setTrangThaiPhong(TrangThaiPhong.HOAT_DONG);
        phong.setNgayTao(LocalDateTime.now());
        Phong phongDaLuu = phongRepository.save(phong);
        luuDanhSachGiuong(phongDaLuu, dto.getDanhSachGiuong());
        return phongDaLuu;
    }


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
    public void capNhatPhong(Long id, PhongDTO dto) {
        Phong phong = phongRepository.findById(id).orElseThrow(() -> new RuntimeException("Khong tim thay phong"));
        BeanUtils.copyProperties(dto, phong, "id", "trangThaiPhong", "maHomeStay", "danhSachGiuong");
        phongRepository.save(phong);
        luuDanhSachGiuong(phong, dto.getDanhSachGiuong());
    }


    public void luuDanhSachGiuong(Phong phong, List<ChiTietGiuongDTO> chiTietGiuongDTOList) {
        if(chiTietGiuongDTOList == null){
            return;
        }
        List<ChiTietGiuong> danhSachMoi = new ArrayList<>();
        for(ChiTietGiuongDTO chiTietGiuongDTO : chiTietGiuongDTOList){
            LoaiGiuong loaiGiuong = loaiGiuongRepository.findById(chiTietGiuongDTO.getMaLoaiGiuong()).orElseThrow(() -> new RuntimeException("Khong tim thay loai giuong"));
            ChiTietGiuong chiTietGiuong = new ChiTietGiuong();
            chiTietGiuong.setPhong(phong);
            chiTietGiuong.setLoaiGiuong(loaiGiuong);
            chiTietGiuong.setSoLuong(chiTietGiuongDTO.getSoLuong());
            danhSachMoi.add(chiTietGiuong);
        }
        chiTietGiuongRepository.deleteByPhong(phong);
        chiTietGiuongRepository.saveAll(danhSachMoi);
    }

}
