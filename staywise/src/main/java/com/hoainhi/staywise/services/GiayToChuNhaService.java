package com.hoainhi.staywise.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public interface GiayToChuNhaService {
    void luuGiayPhepKinhDoanh(
            String email,
            MultipartFile giayPhepKinhDoanh
    ) throws IOException ;
}
