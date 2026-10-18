package com.hoainhi.staywise.services.serviceimpl;
import com.hoainhi.staywise.dtos.LoaiGiuongDTO;
import com.hoainhi.staywise.entities.LoaiGiuong;
import com.hoainhi.staywise.enums.TrangThai;
import com.hoainhi.staywise.reponsitories.LoaiGiuongRepository;
import com.hoainhi.staywise.services.LoaiGiuongService;
import com.hoainhi.staywise.services.cloudinary.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class LoaiGiuongServiceImpl implements LoaiGiuongService {
    @Autowired
    private LoaiGiuongRepository loaiGiuongRepository;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Override
    public LoaiGiuong themLoaiGiuong(LoaiGiuong loaiGiuong) {
        return loaiGiuongRepository.save(loaiGiuong);
    }

    @Override
    public Page<LoaiGiuong> timKiem(String tuKhoa, int trang, int kichThuoc) {
        Pageable pageable = PageRequest.of(trang, kichThuoc, Sort.by("id").descending());
        if(tuKhoa == null || "".equals(tuKhoa.trim())){
             return  loaiGiuongRepository.findByTrangThaiNot(TrangThai.DA_XOA, pageable);
        }
        return loaiGiuongRepository.findByTenGiuongContainingIgnoreCase(tuKhoa, TrangThai.DA_XOA, pageable);
    }

    @Override
    public void xoa(Long id) {
        LoaiGiuong loaiGiuong = loaiGiuongRepository.findById(id).orElseThrow();
        loaiGiuong.setTrangThai(TrangThai.DA_XOA);
        loaiGiuongRepository.save(loaiGiuong);

    }

    @Override
    public LoaiGiuong getById(Long id) {
        return loaiGiuongRepository.findById(id).orElseThrow(() -> new RuntimeException("khong tim thay"));
    }

    @Override
    public String saveLoaiGiuong(LoaiGiuongDTO loaiGiuongDTO, MultipartFile file) throws IOException {
        boolean themMoi = loaiGiuongDTO.getId() == null;

        if(themMoi && (loaiGiuongDTO.getTenGiuong() == null || loaiGiuongDTO.getTenGiuong().isBlank())){
            throw new RuntimeException("Ten giuong khong duoc de trong");
        }
        LoaiGiuong loaiGiuong;
        if(themMoi){
            loaiGiuong = new LoaiGiuong();
            loaiGiuong.setTrangThai(TrangThai.DANG_HOAT_DONG);
        }else {
         loaiGiuong = loaiGiuongRepository.findById(loaiGiuongDTO.getId()).orElseThrow(() -> new RuntimeException("Khong tim thay"));

        }

        if(loaiGiuongDTO.getTenGiuong() != null && !loaiGiuongDTO.getTenGiuong().isBlank()){
            loaiGiuong.setTenGiuong(loaiGiuongDTO.getTenGiuong());
        }
        if(file != null && !file.isEmpty()){
            String url = cloudinaryService.taiAnh(file);
            loaiGiuong.setHinhAnh(url);
        }
        loaiGiuongRepository.save(loaiGiuong);
        return themMoi ? "Them thong tin thanh cong" : "Cap nhat thong tin thanh cong";
    }
}
