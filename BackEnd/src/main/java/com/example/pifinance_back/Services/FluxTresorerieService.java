package com.example.pifinance_back.Services;

import com.example.pifinance_back.Entities.FluxTresorerie;

import java.util.List;

public interface FluxTresorerieService {
    FluxTresorerie addFluxTresorerie(FluxTresorerie fluxTresorerie, int projetid);
    List<FluxTresorerie> retrieveAllFluxTresorerie();
    void removeFluxTresorerie(int idFluxTresorerie );
    FluxTresorerie updateFluxTresorerie(FluxTresorerie fluxTresorerie, int projetid);
    FluxTresorerie retrieveFluxTresorerie(int idFluxTresorerie);
}
