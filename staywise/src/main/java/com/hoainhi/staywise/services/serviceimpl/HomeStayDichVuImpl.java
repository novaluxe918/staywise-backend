package com.hoainhi.staywise.services.serviceimpl;

import com.hoainhi.staywise.entities.HomeStayDichVu;
import com.hoainhi.staywise.reponsitories.HomeStayDichVuRepository;
import com.hoainhi.staywise.services.HomeStayDichVuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HomeStayDichVuImpl implements HomeStayDichVuService {
    @Autowired
    private HomeStayDichVuRepository homeStayDichVuRepository;

    @Override
    public HomeStayDichVu themHomeStayDichVu(HomeStayDichVu homeStayDichVu) {
        return homeStayDichVuRepository.save(homeStayDichVu);
    }

    @Override
    public List<HomeStayDichVu> findAllById(List<Long> ids) {
        return homeStayDichVuRepository.findAllById(ids);
    }
}
