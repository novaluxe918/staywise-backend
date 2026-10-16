package com.hoainhi.staywise.services.serviceimpl;

import com.hoainhi.staywise.dtos.TheLoaiTienTichDTO;
import com.hoainhi.staywise.entities.TheLoaiTienIch;
import com.hoainhi.staywise.enums.TrangThai;
import com.hoainhi.staywise.reponsitories.TheLoaiTienIchRepository;
import com.hoainhi.staywise.services.TheLoaiTienIchService;
import com.hoainhi.staywise.services.cloudinary.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class TheLoaiTienIchServiceImpl implements TheLoaiTienIchService {
    @Autowired
    private TheLoaiTienIchRepository theLoaiTienIchRepository;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Override
    public TheLoaiTienIch themTheLoai(TheLoaiTienIch theLoaiTienIch) {
        return theLoaiTienIchRepository.save(theLoaiTienIch);
    }

    @Override
    public Page<TheLoaiTienIch> timKiem(String tuKhoa, int trang, int kichThuoc) {
        Pageable pageable = PageRequest.of(trang, kichThuoc, Sort.by("id").descending());
        if(tuKhoa == null || "".equals(tuKhoa.trim())){
           return theLoaiTienIchRepository.findByTrangThaiNot(TrangThai.DA_XOA, pageable);
        }
        return theLoaiTienIchRepository.findByTenTheLoaiContainingIgnoreCase(tuKhoa, TrangThai.DA_XOA, pageable);
    }

    @Override
    public void xoa(Long id) {
        TheLoaiTienIch theLoaiTienIch = theLoaiTienIchRepository.findById(id).orElseThrow(() ->
                new RuntimeException(
                        "Không tìm thấy thể loại tiện ích"
                ));
        theLoaiTienIch.setTrangThai(TrangThai.DA_XOA);
        theLoaiTienIchRepository.save(theLoaiTienIch);

    }

    @Override
    public TheLoaiTienIch getById(Long id) {
        return theLoaiTienIchRepository.findById(id).orElseThrow(() -> new RuntimeException("khong tim thay"));
    }

    @Override
    @Transactional
    public String saveTheLoai(TheLoaiTienTichDTO theLoaiTienTichDTO, MultipartFile file) throws IOException {
        boolean themMoi = theLoaiTienTichDTO.getId() == null;

        if(themMoi && (theLoaiTienTichDTO.getTenTheLoai() == null || theLoaiTienTichDTO.getTenTheLoai().isBlank()) ){
            throw new RuntimeException(
                    "Tên thể loại không được để trống");

        }
        TheLoaiTienIch tienIch;
        if(themMoi){
            tienIch = new TheLoaiTienIch();
            tienIch.setTrangThai(TrangThai.DANG_HOAT_DONG);
        }else {
            tienIch = theLoaiTienIchRepository.findById(theLoaiTienTichDTO.getId()).orElseThrow(() -> new RuntimeException("Khong tim thaty"));

        }
        if (theLoaiTienTichDTO.getTenTheLoai() != null
                && !theLoaiTienTichDTO.getTenTheLoai().isBlank()) {
            tienIch.setTenTheLoai(theLoaiTienTichDTO.getTenTheLoai());
        }

        if(file != null && !file.isEmpty()){
             String url = cloudinaryService.taiAnh(file);
             tienIch.setHinhAnh(url);

        }
        theLoaiTienIchRepository.save(tienIch);
        return themMoi ? "Them thanh cong" : "Cap nhat thanh cong";
    }
}
