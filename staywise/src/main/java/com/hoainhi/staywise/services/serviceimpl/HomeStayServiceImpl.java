package com.hoainhi.staywise.services.serviceimpl;

import com.hoainhi.staywise.dtos.HomeStayDTO;
import com.hoainhi.staywise.entities.*;
import com.hoainhi.staywise.enums.TrangThaiHomeStay;
import com.hoainhi.staywise.enums.VaiTro;
import com.hoainhi.staywise.reponsitories.HomeStayRepository;
import com.hoainhi.staywise.reponsitories.NguoiDungRepository;
import com.hoainhi.staywise.services.HomeStayService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class HomeStayServiceImpl implements HomeStayService {

    @Autowired
    private HomeStayRepository homeStayRepository;

    @Autowired
    private NguoiDungRepository nguoiDungRepository;

    @Autowired
    private TienIchHomeServiceImpl tienIchHomeService;

    @Autowired
    private HomeStayDichVuImpl homeStayDichVu;

    @Override
    public void themHomeStay(HomeStayDTO homeStayDTO, Long maChuHomeStay) {
        HomeStay homeStay = new HomeStay();
        BeanUtils.copyProperties(homeStayDTO, homeStay);
        NguoiDung chuHomeStay = nguoiDungRepository.findByIdAndVaiTro(maChuHomeStay , VaiTro.OWNER).orElseThrow(() -> new RuntimeException("Không tìm thấy chủ HomeStay"));
        homeStay.setNguoiDung(chuHomeStay);
        homeStay.setTrangThaiHomeStay(TrangThaiHomeStay.CHO_DUYET);
        homeStay.setNgayDangKy(LocalDate.now());
        homeStayRepository.save(homeStay);
        for(Long item : homeStayDTO.getDanhSachTienIchHS() ){
            TienIch tienIch = new TienIch();
            tienIch.setId(item);
            TienIchHomeStay tienIchHomeStay = new TienIchHomeStay();
            tienIchHomeStay.setHomeStay(homeStay);
            tienIchHomeStay.setTienIch(tienIch);
            tienIchHomeService.themTienIchHomeStay(tienIchHomeStay);
        }

        for(Long item : homeStayDTO.getDanhSachDichVuHS()){
            DichVu dichVu = new DichVu();
            dichVu.setId(item);
            HomeStayDichVu stayDichVu = new HomeStayDichVu();
            stayDichVu.setHomeStay(homeStay);
            stayDichVu.setDichVu(dichVu);
            homeStayDichVu.themHomeStayDichVu(stayDichVu);
        }
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


}
