package com.example.pifinance_back.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProjetInvestissement implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String nom;
    private String description;
    private LocalDate date_debut;
    private LocalDate date_fin;
    private Double cout_initial;
    private float taux_interet;
    private String localisation;
    private String responsable_projet;
    @Enumerated(EnumType.STRING)
    private StatutProjet statutProjet;
    @OneToMany(mappedBy="projetInvestissement",cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<FluxTresorerie> fluxTresoreries;
}
