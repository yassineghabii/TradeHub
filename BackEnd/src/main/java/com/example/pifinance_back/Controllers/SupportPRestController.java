package com.example.pifinance_back.Controllers;

import com.example.pifinance_back.Entities.SupportP;
import com.example.pifinance_back.Entities.Type;
import com.example.pifinance_back.Services.SupportPService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/SupportP")
@CrossOrigin(origins = "http://localhost:4200")
public class SupportPRestController {
    private SupportPService supportPService;
    @PostMapping("/add")
    public SupportP addSupportP(@RequestBody SupportP supportP) {
        supportP.setDate_publication(LocalDate.now());
        return supportPService.addSupportP(supportP);
    }
    @GetMapping("/all")
    public List<SupportP> retrieveAllSupportP() {
        return supportPService.retrieveAllSupportP();
    }

    @DeleteMapping("/delete/{id}")
    public void removeSupportP(@PathVariable("id") int idSupportP) {
        supportPService.removeSupportP(idSupportP);
    }

    @PutMapping("/update")
    public SupportP updateSupportP(@RequestBody SupportP supportP) {
       SupportP supportP1=supportPService.retrieveSupportP(supportP.getId());
       supportP.setDate_publication(supportP1.getDate_publication());
        return supportPService.updateSupportP(supportP);
    }
    @GetMapping("/get/{id}")
    public SupportP retrieveSupportP(@PathVariable("id") int idSupportP) {
        return supportPService.retrieveSupportP(idSupportP);}
    @GetMapping("/getpartype/{type}")
    public List<SupportP> retrieveSupportPpartype(@PathVariable("type") Type type) {
        return supportPService.TrierSupportparType(type);}
    @GetMapping("/getTypeVideo")
    public Long countByTypeVideo() {
        return supportPService.countByTypeVideo();
    }

    @GetMapping("/getTypeLivre")
    public Long countByTypeLivre() {
        return supportPService.countByTypeLivre();
    }

    @GetMapping("/getTypeArticle")
    public Long countByTypeArticle() {
        return supportPService.countByTypeArticle();
    }

}
