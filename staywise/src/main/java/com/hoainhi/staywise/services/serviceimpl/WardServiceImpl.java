package com.hoainhi.staywise.services.serviceimpl;

import com.hoainhi.staywise.dtos.ProvinceDTO;
import com.hoainhi.staywise.dtos.WardsDTO;
import com.hoainhi.staywise.services.WardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class WardServiceImpl implements WardService {
    @Autowired
    private RestTemplate restTemplate;

    @Override
    public List<WardsDTO> getWardsByProvince(Integer provinceCode) {
        String url = "https://provinces.open-api.vn/api/v2/p/"
                + provinceCode + "?depth=2";

        ProvinceDTO province = restTemplate.getForObject(
                url, ProvinceDTO.class
        );

        if (province == null || province.getWardsDTOList() == null) {
            return List.of();
        }

        return province.getWardsDTOList();
    }
}
