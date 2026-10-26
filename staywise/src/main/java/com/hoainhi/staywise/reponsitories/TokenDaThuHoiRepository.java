package com.hoainhi.staywise.reponsitories;

import com.hoainhi.staywise.entities.TokenDaThuHoi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenDaThuHoiRepository extends JpaRepository<TokenDaThuHoi, Long> {
    boolean existsByToken(String token);
}
