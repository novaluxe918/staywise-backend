package com.hoainhi.staywise.services;

import com.hoainhi.staywise.dtos.HinhAnhHomeStayDTO;
import com.hoainhi.staywise.entities.HinhAnhHomeStay;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public interface HinhAnhHomeStayService {
    List<HinhAnhHomeStayDTO> themAnh(List<MultipartFile> files, Long maHomeStay) throws IOException;
}
