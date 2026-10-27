package com.hoainhi.staywise.services.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class DiaChiHanhChinhService {
    @Autowired
    private RestClient restClient;

    public DiaChiHanhChinhService(){
        this.restClient = RestClient.builder()
                .baseUrl("https://provinces.open-api.vn/api/v2")
                .build();
    }

    public String layDanhSachTinhThanh(){
        return restClient.get()
                .uri("/p/")
                .retrieve()
                .body(String.class);
    }

    public String layPhuongXaTheoTinh(Integer maTinh){
        return restClient.get()
        .uri("/p/{maTinh}?depth=2", maTinh)
                .retrieve()
                .body(String.class);
    }

}
