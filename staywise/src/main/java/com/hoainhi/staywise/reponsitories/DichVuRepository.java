package com.hoainhi.staywise.reponsitories;

import com.hoainhi.staywise.entities.DichVu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DichVuRepository extends JpaRepository<DichVu, Long> {
}
