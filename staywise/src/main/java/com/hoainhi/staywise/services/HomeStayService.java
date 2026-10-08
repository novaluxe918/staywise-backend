package com.hoainhi.staywise.services;

import com.hoainhi.staywise.dtos.HomeStayDTO;
import com.hoainhi.staywise.entities.HomeStay;
import org.springframework.stereotype.Service;

@Service
public interface HomeStayService {
    void themHomeStay(HomeStayDTO homeStayDTO , Long maChuHomeStay);
}
