package com.hoainhi.staywise.services.serviceimpl;

import com.hoainhi.staywise.entities.NguoiDung;
import com.hoainhi.staywise.enums.VaiTro;
import com.hoainhi.staywise.reponsitories.NguoiDungRepository;
import com.hoainhi.staywise.services.GiayToChuNhaService;
import com.hoainhi.staywise.services.cloudinary.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class GiayToChuNhaServiceImpl implements GiayToChuNhaService {
    @Autowired
    private NguoiDungRepository nguoiDungRepository;

    @Autowired
    private CloudinaryService cloudinaryService;



    @Override
    public void luuGiayPhepKinhDoanh(String email, MultipartFile giayPhepKinhDoanh) throws IOException {
        NguoiDung nguoiDung = nguoiDungRepository.findByEmail(email)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Không tìm thấy người dùng"));

        if (nguoiDung.getVaiTro() != VaiTro.OWNER) {
            throw new IllegalArgumentException(
                    "Chỉ chủ HomeStay mới được đăng ký giấy tờ");
        }

        if (giayPhepKinhDoanh == null
                || giayPhepKinhDoanh.isEmpty()) {
            throw new IllegalArgumentException(
                    "Vui lòng tải giấy phép kinh doanh");
        }

        String urlGiayPhep =
                cloudinaryService.taiAnh(giayPhepKinhDoanh);

        nguoiDung.setAnhGiayPhepKinhDoanh(urlGiayPhep);

        nguoiDungRepository.save(nguoiDung);
    }
}
