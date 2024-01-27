package com.example.pifinance_back.Repositories;

import com.example.pifinance_back.Entities.ProjetInvestissement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProjetInvestissementRepository extends JpaRepository<ProjetInvestissement,Integer> {
    @Query("select p from ProjetInvestissement p where p.localisation = ?1")
    List<ProjetInvestissement> findByLocalisation(String localisation);

}
