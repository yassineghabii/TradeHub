package com.example.pifinance_back.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@Builder
@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"Client", "OrdreAchat", "OrdreVente", "Transaction", "Titre"})

public class Portfolio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_portfolio;

    @ElementCollection
    private List<String> symboles; // Contient les valeurs des symboles

    @ElementCollection
    private List<Integer> quantites; // Contient les valeurs des quantités

    // Assurez-vous que symboles et quantites ont la même taille
    public void setSymboles(List<String> symboles) {
        if (quantites != null && !quantites.isEmpty() && quantites.size() != symboles.size()) {
            throw new IllegalArgumentException("Les listes de symboles et de quantités doivent avoir la même taille.");
        }
        this.symboles = symboles;
    }

    public void setQuantites(List<Integer> quantites) {
        if (symboles != null && !symboles.isEmpty() && symboles.size() != quantites.size()) {
            throw new IllegalArgumentException("Les listes de symboles et de quantités doivent avoir la même taille.");
        }
        this.quantites = quantites;
    }
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    @JsonIgnore
    private Client client;
}
