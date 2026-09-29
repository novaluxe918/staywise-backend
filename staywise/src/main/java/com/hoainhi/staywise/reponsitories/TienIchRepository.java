package com.hoainhi.staywise.reponsitories;

import com.hoainhi.staywise.entities.TienIch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TienIchRepository extends JpaRepository<TienIch, Long> {
}
