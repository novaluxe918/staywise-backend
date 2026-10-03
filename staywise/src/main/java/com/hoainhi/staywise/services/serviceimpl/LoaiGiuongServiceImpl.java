package com.hoainhi.staywise.services.serviceimpl;
import com.hoainhi.staywise.entities.LoaiGiuong;
import com.hoainhi.staywise.enums.TrangThai;
import com.hoainhi.staywise.reponsitories.LoaiGiuongRepository;
import com.hoainhi.staywise.services.LoaiGiuongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class LoaiGiuongServiceImpl implements LoaiGiuongService {
    @Autowired
    private LoaiGiuongRepository loaiGiuongRepository;
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
}
