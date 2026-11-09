package com.hoainhi.staywise.services;

import com.hoainhi.staywise.dtos.NguoiDungDTO;
import com.hoainhi.staywise.dtos.NguoiDungResponseDTO;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface NguoiDungService {
    public void capNhatThongTin(String email, NguoiDungDTO dto) throws IOException;
    NguoiDungResponseDTO layThongTin(String email);
}
