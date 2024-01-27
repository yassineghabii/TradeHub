package com.example.pifinance_back.Controllers;

import com.example.pifinance_back.Entities.FluxTresorerie;
import com.example.pifinance_back.Entities.ProjetInvestissement;
import com.example.pifinance_back.Services.FluxTresorerieService;
import com.example.pifinance_back.Services.ProjetInvestissementService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/fluxtresorerie")
public class FluxTresorerieRestController {
    private FluxTresorerieService fluxTresorerieService;
     private ProjetInvestissementService projetInvestissementService;
    @GetMapping("/all")
    List<FluxTresorerie> retrieveAllFluxTresorerie() {

        return fluxTresorerieService.retrieveAllFluxTresorerie();
    }
    @PostMapping("/add/{id}")
    FluxTresorerie AddFluxTresorerie(@RequestBody FluxTresorerie fluxTresorerie,@PathVariable("id") int idProjet){
        ProjetInvestissement projet= projetInvestissementService.retrievePojetInvestissement(idProjet);
        fluxTresorerie.setRevenu(projet.getTaux_interet()*fluxTresorerie.getCout_investi()* fluxTresorerie.getPeriode_investissement());
        projet.setCout_initial(projet.getCout_initial()- fluxTresorerie.getCout_investi());
    return fluxTresorerieService.addFluxTresorerie(fluxTresorerie,idProjet);
    }
    @DeleteMapping("/delete/{id}")
    void removeFluxTresorerie (@PathVariable("id") Integer idFluxTresorerie){

        fluxTresorerieService.removeFluxTresorerie(idFluxTresorerie);
    }
    @GetMapping("/get/{id}")
    FluxTresorerie retrieveFluxTresorerie (@PathVariable("id") Integer idFluxTresorerie){
        return fluxTresorerieService.retrieveFluxTresorerie(idFluxTresorerie);
    }
    @PutMapping("/update/{id}")
    FluxTresorerie updateFluxTresorerie (@RequestBody FluxTresorerie fluxTresorerie, @PathVariable("id") int idProjet){
        return fluxTresorerieService.updateFluxTresorerie(fluxTresorerie,idProjet);
    }

}
