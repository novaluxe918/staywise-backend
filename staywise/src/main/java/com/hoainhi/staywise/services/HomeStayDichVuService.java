package com.hoainhi.staywise.services;

import com.hoainhi.staywise.entities.HomeStayDichVu;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface HomeStayDichVuService {
    HomeStayDichVu themHomeStayDichVu(HomeStayDichVu homeStayDichVu);
    List<HomeStayDichVu> findAllById(List<Long> ids);
}
