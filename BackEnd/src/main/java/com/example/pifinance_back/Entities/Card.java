package com.example.pifinance_back.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

@Data
@Builder
@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Card implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_card;
    @Column(unique = true)
    private String number;
    private Integer  exp_month;
    private Integer  exp_year;
    private String cvc;

    @JsonProperty("type_de_carte")
    private String CardType;

    @JsonProperty("ville")
    private String ville;

    @JsonProperty("code_postal")
    private String codePostal;
    @Enumerated(EnumType.STRING)
    @JsonProperty("pays")
    private Country pays;

    @JsonProperty("adresse")
    private String adresseDeFacturation;
    @JsonProperty("currency")
    private Currency currency;

    @JsonProperty("prenom_nom")
    private String prenomNom;

    @OneToOne(mappedBy = "card")
    private Wallet wallet;
    @JsonIgnore
    @OneToMany(mappedBy = "card", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HistoriqueChargement> historiqueChargements = new ArrayList<>();

}
