package com.example.pifinance_back.Controllers;

import com.example.pifinance_back.Entities.ProjetInvestissement;
import com.example.pifinance_back.Services.ProjetInvestissementService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/ProjetInvestissement")
@CrossOrigin(origins = "http://localhost:4200")
public class ProjetInvestissementRestController {
    private ProjetInvestissementService projetInvestissementService;
    @GetMapping("/all")
    List<ProjetInvestissement> retrieveAllPojetInvestissement() {
        return projetInvestissementService.retrieveAllPojetInvestissement();
    }
    @PostMapping("/add")
    ProjetInvestissement AddPojetInvestissement(@RequestBody ProjetInvestissement projetInvestissement){
        return projetInvestissementService.addProjetInvestissement(projetInvestissement);
    }
    @DeleteMapping("/delete/{id}")
    void removePojetInvestissement(@PathVariable("id") Integer idProjetInvestissement){
        projetInvestissementService.removePojetInvestissement(idProjetInvestissement);
    }
    @GetMapping("/get/{id}")
    ProjetInvestissement retrievPojetInvestissement(@PathVariable("id") Integer idProjetInvestissement){
        return projetInvestissementService.retrievePojetInvestissement(idProjetInvestissement);
    }
    @PutMapping("/update")
    ProjetInvestissement updatePojetInvestissement (@RequestBody ProjetInvestissement projetInvestissement){
        return projetInvestissementService.updatePojetInvestissement(projetInvestissement);
    }
    @GetMapping("/getbyloca/{localisation}")
    List<ProjetInvestissement> retrievPojetBylocalisation(@PathVariable("localisation") String Localisation){
        return projetInvestissementService.retrieveProjetByLcocalisation(Localisation);
    }

}
