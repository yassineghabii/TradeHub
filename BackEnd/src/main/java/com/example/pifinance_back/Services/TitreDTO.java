package com.example.pifinance_back.Services;

import com.example.pifinance_back.Entities.Titre;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TitreDTO {
    private int idTitre;
    private String symbole;
    private String nom;
    private LocalDateTime dateCreation;
    private double prixOuverture;
    private double prixPlusHaut;
    private double prixPlusBas;
    private double prixActuel;
    private Double prixCloture;
    private int quantite;
    private LocalDateTime dateMaj;

    static TitreDTO mapToDTO(Titre titre) {
        TitreDTO dto = new TitreDTO();
        dto.setIdTitre(titre.getIdTitre());
        dto.setSymbole(titre.getSymbole());
        dto.setNom(titre.getNom());
        dto.setDateCreation(titre.getDate_Creation());
        dto.setPrixOuverture(titre.getPrixOuverture());
        dto.setPrixPlusHaut(titre.getPrixPlusHaut());
        dto.setPrixPlusBas(titre.getPrixPlusBas());
        dto.setPrixActuel(titre.getPrixActuel());
        dto.setPrixCloture(titre.getPrixCloture());
        dto.setQuantite(titre.getQuantite());
        dto.setDateMaj(titre.getDateMaj());
        return dto;
    }
}