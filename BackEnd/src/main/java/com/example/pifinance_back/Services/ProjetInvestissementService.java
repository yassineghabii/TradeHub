package com.example.pifinance_back.Services;

import com.example.pifinance_back.Entities.ProjetInvestissement;

import java.util.List;

public interface ProjetInvestissementService {
    ProjetInvestissement addProjetInvestissement(ProjetInvestissement projetInvestissement);
    List<ProjetInvestissement> retrieveAllPojetInvestissement();
    void removePojetInvestissement(int idProjetInvestissement);
    ProjetInvestissement updatePojetInvestissement(ProjetInvestissement projetInvestissement);
    ProjetInvestissement retrievePojetInvestissement(int idProjetInvestissement);
    List<ProjetInvestissement> retrieveProjetByLcocalisation(String localisation);
}
