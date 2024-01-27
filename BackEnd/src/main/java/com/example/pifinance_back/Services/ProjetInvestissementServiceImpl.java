package com.example.pifinance_back.Services;

import com.example.pifinance_back.Entities.ProjetInvestissement;
import com.example.pifinance_back.Repositories.ProjetInvestissementRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
@Service
@AllArgsConstructor
public class ProjetInvestissementServiceImpl implements ProjetInvestissementService{
    private ProjetInvestissementRepository projetInvestissementRepository;
    @Override
    public ProjetInvestissement addProjetInvestissement(ProjetInvestissement projetInvestissement) {
        LocalDate d1 = projetInvestissement.getDate_debut();
        LocalDate d2 = projetInvestissement.getDate_fin();
        LocalDate d = LocalDate.now();
        if (d1.isAfter(d)&& d2.isAfter(d1)) {
            projetInvestissementRepository.save(projetInvestissement);
        } else {
            if (!d1.isAfter(d)) {
                throw new IllegalArgumentException("La date de début doit être supérieure à la date d'aujourd'hui.");
            } else {
                throw new IllegalArgumentException("La date de fin doit être supérieure à la date de début.");
            }
        }

        return null;
    }

    @Override
    public List<ProjetInvestissement> retrieveAllPojetInvestissement() {
        return projetInvestissementRepository.findAll();
    }

    @Override
    public void removePojetInvestissement(int idProjetInvestissement) {
        projetInvestissementRepository.deleteById(idProjetInvestissement);
    }

    @Override
    public ProjetInvestissement updatePojetInvestissement(ProjetInvestissement projetInvestissement) {
        return projetInvestissementRepository.save(projetInvestissement);
    }

    @Override
    public ProjetInvestissement retrievePojetInvestissement(int idProjetInvestissement) {
        return projetInvestissementRepository.findById(idProjetInvestissement).orElse(null);
    }

    @Override
    public List<ProjetInvestissement> retrieveProjetByLcocalisation(String localisation) {
        return projetInvestissementRepository.findByLocalisation(localisation);
    }
}
