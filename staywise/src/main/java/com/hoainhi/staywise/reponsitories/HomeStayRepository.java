package com.hoainhi.staywise.reponsitories;

import com.hoainhi.staywise.dtos.HomeStayOptionDTO;
import com.hoainhi.staywise.entities.HomeStay;
import com.hoainhi.staywise.enums.TrangThaiHomeStay;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HomeStayRepository extends JpaRepository<HomeStay, Long> {
   Page<HomeStay> findByTenHomeStayContaining(String tuKhoa, Pageable pageable);
   Page<HomeStay> findByTrangThaiHomeStayNot(TrangThaiHomeStay trangThaiHomeStay, Pageable pageable);

   @Query("""
    SELECT new com.hoainhi.staywise.dtos.HomeStayOptionDTO(
        hs.id,
        hs.tenHomeStay
    )
    FROM HomeStay hs
    WHERE hs.nguoiDung.id = :maChuHomeStay
    AND hs.trangThaiHomeStay = :trangThai
    ORDER BY hs.id DESC
""")
   List<HomeStayOptionDTO> findHomeStayDaDuyet(
           @Param("maChuHomeStay") Long maChuHomeStay,
           @Param("trangThai") TrangThaiHomeStay trangThai
   );
}
