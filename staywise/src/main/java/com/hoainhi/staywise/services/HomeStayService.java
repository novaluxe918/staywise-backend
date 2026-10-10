package com.hoainhi.staywise.services;

import com.hoainhi.staywise.dtos.HomeStayDTO;
import com.hoainhi.staywise.entities.HomeStay;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public interface HomeStayService {
    void themHomeStay(HomeStayDTO homeStayDTO , Long maChuHomeStay);
    Page<HomeStayDTO> showAll(String tuKhoa, int trang, int kichThuoc);
    HomeStay getById(Long id);
    HomeStay duyetHomeStay(Long id);
}
