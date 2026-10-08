package com.hoainhi.staywise.services.serviceimpl;


import com.hoainhi.staywise.entities.TienIchHomeStay;
import com.hoainhi.staywise.reponsitories.TienIchHomeStayRepository;
import com.hoainhi.staywise.reponsitories.TienIchRepository;
import com.hoainhi.staywise.services.TienIchHomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TienIchHomeServiceImpl implements TienIchHomeService {
    @Autowired
    private TienIchHomeStayRepository tienIchHomeStayRepository;

    @Autowired
    private TienIchRepository tienIchRepository;

    @Override
    public TienIchHomeStay themTienIchHomeStay(TienIchHomeStay tienIchHomeStay) {

        return tienIchHomeStayRepository.save(tienIchHomeStay);
    }


}
