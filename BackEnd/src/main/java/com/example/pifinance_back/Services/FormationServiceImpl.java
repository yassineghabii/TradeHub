package com.example.pifinance_back.Services;

import com.example.pifinance_back.Entities.Client;
import com.example.pifinance_back.Entities.Formation;
import com.example.pifinance_back.Repositories.FormationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FormationServiceImpl implements FormationService {
    private FormationRepository formationRepository;

    @Override
    public Formation addFormation(Formation formation) {

        LocalDate d1 = formation.getDate_debut();
        LocalDate d2 = formation.getDate_fin();
        LocalDate d = LocalDate.now();
        if (d1.isAfter(d) && d2.isAfter(d1)) {
            formationRepository.save(formation);
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
    public List<Formation> retrieveAllFormation() {
        return formationRepository.findAll();
    }

    @Override
    public void removeFormation(int idFormation) {
        formationRepository.deleteById(idFormation);
    }

    @Override
    public Formation updateFormation(Formation formation) {
        return formationRepository.save(formation);
    }

    @Override
    public Formation retrieveFormation(int idFormation) {
        return formationRepository.findById(idFormation).orElse(null);
    }

    @Override
    public List<Formation> findByDateBetween(LocalDate startDate, LocalDate endDate) {
        return formationRepository.findByDate_debutAndDate_finBetween(startDate, endDate);
    }

    @Override
    public List<Formation> findByOrderByDate_debutAsc() {
        return formationRepository.findByOrderByDate_debutAsc();
    }

    @Override
    public Set<String> getEmailsForFormation(Integer formationId) {
        Optional<Formation> formation = formationRepository.findById(formationId);

        if (formation.isPresent()) {
            Set<Client> clients = formation.get().getClients();
            Set<String> emails = clients.stream()
                    .map(Client::getEmail)
                    .collect(Collectors.toSet());
            return emails;
        } else {
            return new HashSet<>();
        }
    }
}
