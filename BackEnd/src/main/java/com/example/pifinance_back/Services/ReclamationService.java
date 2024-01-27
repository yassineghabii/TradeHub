package com.example.pifinance_back.Services;

import com.example.pifinance_back.Entities.Reclamation;
import com.example.pifinance_back.Entities.statusReclamation;

import java.util.List;

public interface ReclamationService {
    Reclamation addReclamation(Reclamation reclamation);

    List<Reclamation> getAllReclamations();

    void deleteReclamation(int reclamationId);

    Reclamation updateReclamation(Reclamation reclamation);

    Reclamation getReclamationById(int reclamationId);

    List<Reclamation> generateProcessedReclamationsReport();

    List<Reclamation> managePendingReclamations();

    List<Reclamation> archiveResolvedReclamations();

    Reclamation followReclamationStatus(int reclamationId);

    Reclamation updateReclamationStatus(int id, statusReclamation newStatus);
}
