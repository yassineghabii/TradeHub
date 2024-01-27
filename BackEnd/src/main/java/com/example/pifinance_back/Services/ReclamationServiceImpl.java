package com.example.pifinance_back.Services;

import com.example.pifinance_back.Entities.Reclamation;
import com.example.pifinance_back.Entities.statusReclamation;
import com.example.pifinance_back.Repositories.ReclamationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ReclamationServiceImpl implements ReclamationService {

    private final ReclamationRepository reclamationRepository;


    @Override
    public Reclamation addReclamation(Reclamation reclamation) {
        return reclamationRepository.save(reclamation);
    }

    @Override
    public List<Reclamation> getAllReclamations() {
        return reclamationRepository.findAll();
    }

    @Override
    public void deleteReclamation(int reclamationId) {
        reclamationRepository.deleteById(reclamationId);
    }

    @Override
    public Reclamation updateReclamation(Reclamation reclamation) {
        return reclamationRepository.save(reclamation);
    }

    @Override
    public Reclamation getReclamationById(int reclamationId) {
        Optional<Reclamation> optionalReclamation = reclamationRepository.findById(reclamationId);
        return optionalReclamation.orElse(null);
    }

    //En tant qu'administrateur, je veux générer des rapports sur les réclamations traitées
    @Override
    public List<Reclamation> generateProcessedReclamationsReport() {
        // Retrieve all reclamations from the repository
        List<Reclamation> allReclamations = reclamationRepository.findAll();

        // Filter the reclamations to include only those that have been processed
        return allReclamations.stream()
                .filter(reclamation -> reclamation.getStatus() == statusReclamation.RESOLVED)
                .collect(Collectors.toList());
    }

    @Override
    public List<Reclamation> managePendingReclamations() {
        // Récupère toutes les réclamations depuis le référentiel
        List<Reclamation> allReclamations = reclamationRepository.findAll();

        // Filtre les réclamations pour ne sélectionner que celles en attente
        return allReclamations.stream()
                .filter(reclamation -> reclamation.getStatus() == statusReclamation.IN_PROGRESS)
                .collect(Collectors.toList());
    }

    @Override
    public List<Reclamation> archiveResolvedReclamations() {
        // Retrieve all reclamations from the repository
        List<Reclamation> allReclamations = reclamationRepository.findAll();

        // Filter the reclamations to include only those that have been resolved
        List<Reclamation> resolvedReclamations = allReclamations.stream()
                .filter(reclamation -> reclamation.getStatus() == statusReclamation.RESOLVED)
                .collect(Collectors.toList());

        // Perform the archival process for the resolved reclamations
        for (Reclamation reclamation : resolvedReclamations) {
            reclamation.setArchived(true);
            // Save the updated reclamation back to the repository
            reclamationRepository.save(reclamation);
        }

        return resolvedReclamations;
    }

    @Override
    public Reclamation followReclamationStatus(int reclamationId) {
        // Find the reclamation by its ID
        Optional<Reclamation> optionalReclamation = reclamationRepository.findById(reclamationId);
        if (optionalReclamation.isPresent()) {
            return optionalReclamation.get();
        } else {
            throw new ReclamationNotFoundException("Reclamation not found with ID: " + reclamationId);
        }
    }

    public Reclamation updateReclamationStatus(int id, statusReclamation newStatus) {
        Reclamation reclamation = getReclamationById(id);
        if (reclamation != null) {
            reclamation.setStatus(newStatus);

            if (newStatus == statusReclamation.RESOLVED || newStatus == statusReclamation.CLOSED) {
                reclamation.setCompletionTime(new Date());
            }

            return reclamationRepository.save(reclamation);
        } else {
            return null;
        }
    }



}
