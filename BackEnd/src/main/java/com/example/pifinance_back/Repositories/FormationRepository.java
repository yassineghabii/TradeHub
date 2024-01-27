package com.example.pifinance_back.Repositories;

import com.example.pifinance_back.Entities.Formation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface FormationRepository extends JpaRepository<Formation,Integer> {
    @Query("select a from Formation a where a.date_debut between ?1 and ?2 and a.date_fin between ?1 and ?2")
    List<Formation> findByDate_debutAndDate_finBetween(LocalDate startDate, LocalDate endDate);

    @Query("select f from Formation f order by f.date_debut")
    List<Formation> findByOrderByDate_debutAsc();



}
