package com.hoainhi.staywise.services.serviceimpl;

import com.hoainhi.staywise.dtos.HinhAnhHomeStayDTO;
import com.hoainhi.staywise.entities.HinhAnhHomeStay;
import com.hoainhi.staywise.entities.HomeStay;
import com.hoainhi.staywise.reponsitories.HinhAnhHomeStayRepository;
import com.hoainhi.staywise.reponsitories.HomeStayRepository;
import com.hoainhi.staywise.services.HinhAnhHomeStayService;
import com.hoainhi.staywise.services.cloudinary.CloudinaryService;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class HinhAnhHomeStaySeviceImpl implements HinhAnhHomeStayService {
    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private HinhAnhHomeStayRepository hinhAnhHomeStayRepository;

    @Autowired
    private HomeStayRepository homeStayRepository;


    @Override
    @Transactional
    public List<HinhAnhHomeStayDTO> themAnh(List<MultipartFile> files, Long maHomeStay) throws IOException {
        HomeStay homeStay = homeStayRepository.findById(maHomeStay)
                .orElseThrow(() ->
                        new RuntimeException("Không tìm thấy HomeStay"));
        List<HinhAnhHomeStay> hinhAnhHomeStays = new ArrayList<>();
        for(int i = 0 ; i < files.size(); i++ ){
            MultipartFile file = files.get(i);
            String url = cloudinaryService.taiAnh(file);

            HinhAnhHomeStay hinhAnhHomeStay = new HinhAnhHomeStay();
            hinhAnhHomeStay.setHomeStay(homeStay);
            hinhAnhHomeStay.setDuongDan(url);
            hinhAnhHomeStay.setNgayTao(LocalDateTime.now());
            hinhAnhHomeStay.setLaAnhDaiDien(i == 0);

            hinhAnhHomeStays.add(hinhAnhHomeStay);
        }
        List<HinhAnhHomeStay> anhHomeStayList = hinhAnhHomeStayRepository.saveAll(hinhAnhHomeStays);
        List<HinhAnhHomeStayDTO> homeStayDTOList = new ArrayList<>();
        for(HinhAnhHomeStay anh : anhHomeStayList){
            HinhAnhHomeStayDTO hinhAnhHomeStayDTO = new HinhAnhHomeStayDTO();
            BeanUtils.copyProperties(anh, hinhAnhHomeStayDTO);
            homeStayDTOList.add(hinhAnhHomeStayDTO);
        }
        return homeStayDTOList;
    }
}
