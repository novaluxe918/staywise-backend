package com.hoainhi.staywise.services;

import com.hoainhi.staywise.dtos.WardsDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface WardService {
    List<WardsDTO> getWardsByProvince(Integer provinceCode);
}
