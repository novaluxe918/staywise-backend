package com.hoainhi.staywise.services;

import com.hoainhi.staywise.dtos.ProvinceDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProvinceService {
    List<ProvinceDTO> getAllProvinces();
}
