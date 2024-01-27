package com.example.pifinance_back.Services;

import com.example.pifinance_back.Entities.FluxTresorerie;
import com.example.pifinance_back.Entities.ProjetInvestissement;
import com.example.pifinance_back.Repositories.FluxTresorerieRepository;
import com.example.pifinance_back.Repositories.ProjetInvestissementRepository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@AllArgsConstructor
public class FluxTresorerieServiceImpl implements FluxTresorerieService{
    private FluxTresorerieRepository fluxTresorerieRepository;
    private final ProjetInvestissementRepository projetInvestissementRepository;

    @Override
    public FluxTresorerie addFluxTresorerie(FluxTresorerie fluxTresorerie, int projetid) {
        ProjetInvestissement projetInvestissement =projetInvestissementRepository.findById(projetid).get();
        fluxTresorerie.setProjetInvestissement(projetInvestissement);
        fluxTresorerieRepository.save(fluxTresorerie);
        return fluxTresorerie;
    }

    @Override
    public List<FluxTresorerie> retrieveAllFluxTresorerie() {
        return fluxTresorerieRepository.findAll();
    }

    @Override
    public void removeFluxTresorerie(int idFluxTresorerie) {
     fluxTresorerieRepository.deleteById(idFluxTresorerie);
    }

    @Override
    public FluxTresorerie updateFluxTresorerie(FluxTresorerie fluxTresorerie, int projetid) {
        ProjetInvestissement pi =projetInvestissementRepository.findById(projetid).get();
        fluxTresorerie.setProjetInvestissement(pi);
        return  fluxTresorerieRepository.save(fluxTresorerie);
    }

    @Override
    public FluxTresorerie retrieveFluxTresorerie(int idFluxTresorerie) {
        return fluxTresorerieRepository.findById(idFluxTresorerie).orElse(null);
    }


}
