package com.hoainhi.staywise.services.serviceimpl;

import com.hoainhi.staywise.dtos.HomeStayDTO;
import com.hoainhi.staywise.dtos.PhongDTO;
import com.hoainhi.staywise.entities.HomeStay;
import com.hoainhi.staywise.entities.Phong;
import com.hoainhi.staywise.enums.TrangThai;
import com.hoainhi.staywise.enums.TrangThaiHomeStay;
import com.hoainhi.staywise.enums.TrangThaiPhong;
import com.hoainhi.staywise.reponsitories.HomeStayRepository;
import com.hoainhi.staywise.reponsitories.PhongRepository;
import com.hoainhi.staywise.services.PhongService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PhongServiceImpl implements PhongService {
    @Autowired
    private PhongRepository phongRepository;

    @Autowired
    private HomeStayRepository homeStayRepository;

    @Override
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
        return phongRepository.save(phong);
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
    public void capNhatPhong(Long id, PhongDTO dto) {
        Phong phong = phongRepository.findById(id).orElseThrow(() -> new RuntimeException("Khong tim thay phong"));
        BeanUtils.copyProperties(dto, phong, "id", "trangThaiPhong", "maHomeStay");
        phongRepository.save(phong);
    }

}
