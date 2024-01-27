package com.example.pifinance_back.Entities;

import com.example.pifinance_back.Repositories.OrdreAchatRepository;
import com.example.pifinance_back.Repositories.OrdreVenteRepository;
import com.example.pifinance_back.Repositories.TitreRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class TransactionEventListener implements ApplicationListener<TransactionEvent> {
    private final OrdreAchatRepository ordreAchatRepository;
    private final OrdreVenteRepository ordreVenteRepository;
    private final TitreRepository titreRepository;

    public TransactionEventListener(OrdreAchatRepository ordreAchatRepository,
                                    OrdreVenteRepository ordreVenteRepository,
                                    TitreRepository titreRepository) {
        this.ordreAchatRepository = ordreAchatRepository;
        this.ordreVenteRepository = ordreVenteRepository;
        this.titreRepository = titreRepository;
    }

    @Override
    public void onApplicationEvent(TransactionEvent event) {
        Transaction transaction = event.getTransaction();

        // Récupérer le titre en fonction du type d'ordre spécifique à la transaction
        Titre titre = getTitreFromTransaction(transaction);

        if (titre != null) {
            // Mettre à jour le prix actuel avec le prix d'exécution de la transaction
            titre.setPrixActuel(transaction.getPrixExecution());

            // Mettre à jour la date de mise à jour
            titre.setDateMaj(LocalDateTime.now());
            // Mettre à jour le PrixPlusHaut et PrixPlusBas si nécessaire
            updatePrixPlusHautEtBas(titre, transaction.getPrixExecution());

            // Mettre à jour la quantité du titre en fonction des ordres de vente limites
            updateQuantiteTitre(titre);

            // Enregistrez les modifications dans la base de données
            titreRepository.save(titre);
        }
    }

    private Titre getTitreFromTransaction(Transaction transaction) {
        if (transaction.getTypeTransaction() == TypeTransaction.ACHAT_AU_MARCHE || transaction.getTypeTransaction() == TypeTransaction.ACHAT_LIMITE) {
            OrdreAchat ordreAchat = ordreAchatRepository.findByTransaction(transaction);
            return (ordreAchat != null) ? ordreAchat.getTitre() : null;
        } else if (transaction.getTypeTransaction() == TypeTransaction.VENTE_AU_MARCHE || transaction.getTypeTransaction() == TypeTransaction.VENTE_LIMITE) {
            OrdreVente ordreVente = ordreVenteRepository.findByTransaction(transaction);
            return (ordreVente != null) ? ordreVente.getTitre() : null;
        }
        return null;
    }

    private void updatePrixPlusHautEtBas(Titre titre, double prixExecution) {
        if (titreRepository.findByPrixPlusHaut(titre.getPrixPlusHaut()) == null || prixExecution > titre.getPrixPlusHaut()) {
            titre.setPrixPlusHaut(prixExecution);
        }

        if (titreRepository.findByPrixPlusBas(titre.getPrixPlusBas()) == null || prixExecution < titre.getPrixPlusBas()) {
            titre.setPrixPlusBas(prixExecution);
        }
    }
    private void updateQuantiteTitre(Titre titre) {
        // Récupérer tous les ordres de vente limites associés à ce titre
        List<OrdreVente> ordresVenteLimite = ordreVenteRepository.findByTitreAndTypeOrdre(titre, TypeOrdre.LIMITE);

        // Utiliser Stream pour calculer la somme des quantités de tous les ordres de vente limites
        int nouvelleQuantite = ordresVenteLimite.stream().mapToInt(OrdreVente::getQuantite).sum();

        // Mettre à jour la quantité du titre avec la nouvelle valeur calculée
        titre.setQuantite(nouvelleQuantite);
        titreRepository.save(titre);
    }


}
