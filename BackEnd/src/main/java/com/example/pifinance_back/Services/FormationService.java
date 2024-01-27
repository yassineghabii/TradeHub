package com.example.pifinance_back.Services;

import com.example.pifinance_back.Entities.Formation;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface FormationService {
    Formation addFormation(Formation formation);
    List<Formation> retrieveAllFormation();
    void removeFormation(int idFormation );
    Formation updateFormation(Formation formation);
    Formation retrieveFormation(int idFormation);
    List<Formation> findByDateBetween(LocalDate startDate, LocalDate endDate);
    List<Formation> findByOrderByDate_debutAsc();
    Set<String> getEmailsForFormation(Integer formationId);



    }
