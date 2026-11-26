package com.hoainhi.staywise.reponsitories;

import com.hoainhi.staywise.entities.DuDoanGia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DuDoanGiaRepository extends JpaRepository<DuDoanGia, Long> {
    List<DuDoanGia> findAllByOrderByNgayDuDoanDesc();

    List<DuDoanGia> findByPhong_IdAndNgayApDungOrderByNgayDuDoanDesc(
            Long maPhong, LocalDate ngayApDung);

    List<DuDoanGia> findByPhong_IdOrderByNgayDuDoanDesc(
            Long maPhong);

    List<DuDoanGia> findByNgayApDungOrderByNgayDuDoanDesc(
            LocalDate ngayApDung);

    List<DuDoanGia> findByPhong_HomeStay_IdOrderByNgayDuDoanDesc(
            Long maHomeStay
    );
}
