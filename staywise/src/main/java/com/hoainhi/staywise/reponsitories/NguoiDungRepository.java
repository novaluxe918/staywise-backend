package com.hoainhi.staywise.reponsitories;

import com.hoainhi.staywise.entities.NguoiDung;
import com.hoainhi.staywise.enums.VaiTro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NguoiDungRepository extends JpaRepository<NguoiDung, Long> {
    boolean existsByEmail(String email);
    Optional<NguoiDung> findByEmail(String email);
    Optional<NguoiDung> findByIdAndVaiTro(Long id, VaiTro vaiTro);
}
