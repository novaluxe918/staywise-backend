package com.hoainhi.staywise.services;

import com.hoainhi.staywise.dtos.DuDoanGiaRequest;
import com.hoainhi.staywise.response.DuDoanGiaResponse;
import com.hoainhi.staywise.response.LichSuDuDoanGiaResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public interface DuDoanGiaService {

    DuDoanGiaResponse duDoanGia(DuDoanGiaRequest request);
    List<LichSuDuDoanGiaResponse> xemLichSuDuDoanGia(Long maHomeStay);
    List<LichSuDuDoanGiaResponse> timKiemLichSuDuDoanGia(
            Long maPhong, LocalDate ngayApDung);

}
