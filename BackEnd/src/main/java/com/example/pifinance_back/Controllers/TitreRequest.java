package com.example.pifinance_back.Controllers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Data
public class TitreRequest {
    private String symbole;
    private String nom;
    private double prixOuverture;
    private int quantite;
    public TitreRequest() {
    }
    // Getters et Setters
}
