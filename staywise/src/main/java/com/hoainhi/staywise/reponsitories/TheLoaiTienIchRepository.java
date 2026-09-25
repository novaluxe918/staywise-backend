package com.hoainhi.staywise.reponsitories;

import com.hoainhi.staywise.entities.TheLoaiTienIch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TheLoaiTienIchRepository extends JpaRepository<TheLoaiTienIch, Long> {
}
