package com.hoainhi.staywise.services;

import com.hoainhi.staywise.entities.HomeStay;
import com.hoainhi.staywise.entities.TienIchHomeStay;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TienIchHomeService {
    TienIchHomeStay themTienIchHomeStay(TienIchHomeStay tienIchHomeStay);
    List<TienIchHomeStay> findAllById(List<Long> ids);
}
