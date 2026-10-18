package com.hoainhi.staywise.services;

import com.hoainhi.staywise.dtos.HomeStayDTO;
import com.hoainhi.staywise.entities.HomeStay;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public interface HomeStayService {
    Page<HomeStayDTO> showAll(String tuKhoa, int trang, int kichThuoc);
    HomeStay getById(Long id);
    HomeStay duyetHomeStay(Long id);
    HomeStay tuChoiHomeStay(Long id, String lyDoTuChoi);
    void xoa(Long id);
    void ngungKinhDoanh(Long id);
    String saveHomeStay(HomeStayDTO dto, List<MultipartFile> files, Long maChuHomeStay) throws IOException;
}
