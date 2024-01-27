package com.example.pifinance_back.Repositories;

import com.example.pifinance_back.Entities.HistoriqueChargement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository

public interface HistoriqueChargementRepository extends JpaRepository<HistoriqueChargement, Long> {
    List<HistoriqueChargement> findByUserId(Long userId);

}
