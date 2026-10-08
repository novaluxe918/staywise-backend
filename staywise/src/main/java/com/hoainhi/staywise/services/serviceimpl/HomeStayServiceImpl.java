package com.hoainhi.staywise.services.serviceimpl;

import com.hoainhi.staywise.dtos.HomeStayDTO;
import com.hoainhi.staywise.entities.HomeStay;
import com.hoainhi.staywise.entities.NguoiDung;
import com.hoainhi.staywise.entities.TienIch;
import com.hoainhi.staywise.entities.TienIchHomeStay;
import com.hoainhi.staywise.enums.TrangThaiHomeStay;
import com.hoainhi.staywise.enums.VaiTro;
import com.hoainhi.staywise.reponsitories.HomeStayRepository;
import com.hoainhi.staywise.reponsitories.NguoiDungRepository;
import com.hoainhi.staywise.services.HomeStayService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

    private TienIchServiceImpl tienIchService;
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

    }
}
