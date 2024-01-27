package com.example.pifinance_back.Entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class OrdreAchatAuMarcheDTO {
    private int idOrdreA;
    private Double PrixAuMarcheA;
    private int quantite;
    private LocalDateTime dateOrdreA;
    @Enumerated(EnumType.STRING)
    private StatutOrdre Statut;
    private String symbole ;

}