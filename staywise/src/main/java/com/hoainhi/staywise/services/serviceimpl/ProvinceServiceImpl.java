package com.hoainhi.staywise.services.serviceimpl;

import com.hoainhi.staywise.dtos.ProvinceDTO;
import com.hoainhi.staywise.services.ProvinceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class ProvinceServiceImpl implements ProvinceService {
    @Autowired
    private RestTemplate restTemplate;

    @Override
    public List<ProvinceDTO> getAllProvinces() {
        String url = "https://provinces.open-api.vn/api/v2/p/";
        ProvinceDTO[] provinces = restTemplate.getForObject(
                url, ProvinceDTO[].class
        );

        return provinces == null
                ? List.of()
                : Arrays.asList(provinces);
    }
}
