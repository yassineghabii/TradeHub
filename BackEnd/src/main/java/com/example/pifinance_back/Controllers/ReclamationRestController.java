package com.example.pifinance_back.Controllers;

import com.example.pifinance_back.Entities.Reclamation;
import com.example.pifinance_back.Entities.statusReclamation;
import com.example.pifinance_back.Services.ReclamationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/reclamations")
@AllArgsConstructor
public class ReclamationRestController {

    private final ReclamationService reclamationService;


    //En tant qu'utilisateur, je veux pouvoir soumettre une réclamation	Conception	T1 : Planifier le formulaire de réclamation	1h
    @PostMapping("/addReclamation")
    public ResponseEntity<Reclamation> addReclamation(@RequestBody Reclamation reclamation) {
        Reclamation newReclamation = reclamationService.addReclamation(reclamation);
        return ResponseEntity.status(HttpStatus.CREATED).body(newReclamation);
    }

    @GetMapping("/listReclamation")
    public ResponseEntity<List<Reclamation>> getAllReclamations() {
        List<Reclamation> reclamations = reclamationService.getAllReclamations();
        return ResponseEntity.ok(reclamations);
    }

    @GetMapping("/getReclamation/{id}")
    public ResponseEntity<Reclamation> getReclamationById(@PathVariable int id) {
        Reclamation reclamation = reclamationService.getReclamationById(id);
        if (reclamation != null) {
            return ResponseEntity.ok(reclamation);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/updateReclamation/{id}")
    public ResponseEntity<Reclamation> updateReclamation(@PathVariable int id, @RequestBody Reclamation reclamation) {
        if (reclamationService.getReclamationById(id) != null) {
            reclamation.setId(id);
            Reclamation updatedReclamation = reclamationService.updateReclamation(reclamation);
            return ResponseEntity.ok(updatedReclamation);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/deleteReclamation/{id}")
    public ResponseEntity<Void> deleteReclamation(@PathVariable int id) {
        reclamationService.deleteReclamation(id);
        return ResponseEntity.noContent().build();
    }
//En tant qu'administrateur, je veux générer des rapports sur les réclamations traitées
    @GetMapping("/processed")
    public ResponseEntity<List<Reclamation>> getProcessedReclamations() {
        // Call the service method to generate the report for processed reclamations
        List<Reclamation> processedReclamations = reclamationService.generateProcessedReclamationsReport();
        return ResponseEntity.ok(processedReclamations);
    }


//En tant qu'administrateur, je veux gérer les réclamations en attente
    @GetMapping("/pending")
    public ResponseEntity<List<Reclamation>> getPendingReclamations() {
        // Appelle la méthode de service pour gérer les réclamations en attente
        List<Reclamation> pendingReclamations = reclamationService.managePendingReclamations();
        return ResponseEntity.ok(pendingReclamations);
    }

    @PostMapping("/archive/resolved")
    public ResponseEntity<List<Reclamation>> archiveResolvedReclamations() {
        // Call the service method to archive the resolved reclamations
        List<Reclamation> archivedReclamations = reclamationService.archiveResolvedReclamations();
        return ResponseEntity.ok(archivedReclamations);
    }

    @GetMapping("/follow/{reclamationId}")
    public ResponseEntity<Reclamation> followReclamationStatus(@PathVariable int reclamationId) {
        Reclamation reclamation = reclamationService.followReclamationStatus(reclamationId);
        return ResponseEntity.ok(reclamation);
    }

    @PutMapping("/{id}/update-status")
    public ResponseEntity<Reclamation> updateReclamationStatus(
            @PathVariable int id,
            @RequestParam statusReclamation newStatus
    ) {
        Reclamation updatedReclamation = reclamationService.updateReclamationStatus(id, newStatus);
        if (updatedReclamation != null) {
            return ResponseEntity.ok(updatedReclamation);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
