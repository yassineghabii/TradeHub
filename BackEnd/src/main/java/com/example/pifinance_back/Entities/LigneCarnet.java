package com.example.pifinance_back.Entities;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class LigneCarnet {
    private double prixAchat;
    private int quantiteAchat;
    private int nbOrdresAchat;
    private double prixVente;
    private int quantiteVente;
    private int nbOrdresVente;

    // Constructeurs, getters et setters ici...

    // Vous pouvez ajouter des méthodes pour ajouter des ordres d'achat ou de vente
    public void ajouterOrdreAchat(OrdreAchat ordre) {
        this.prixAchat = ordre.getPrixLimiteA();  // Assure que le prix de vente est mis à jour
        this.quantiteAchat += ordre.getQuantite();
        this.nbOrdresAchat++;
    }

    public void ajouterOrdreVente(OrdreVente ordre) {
        this.prixVente = ordre.getPrixLimiteV();  // Assure que le prix de vente est mis à jour
        this.quantiteVente += ordre.getQuantite();
        this.nbOrdresVente++;
    }
}
