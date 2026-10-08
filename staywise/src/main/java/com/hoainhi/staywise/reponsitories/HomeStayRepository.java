package com.hoainhi.staywise.reponsitories;

import com.hoainhi.staywise.entities.HomeStay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HomeStayRepository extends JpaRepository<HomeStay, Long> {
}
