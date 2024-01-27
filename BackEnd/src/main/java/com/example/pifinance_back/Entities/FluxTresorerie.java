package com.example.pifinance_back.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FluxTresorerie implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int Periode_investissement;
    private double revenu;
    private double cout_investi;
    @ManyToOne
    @JsonIgnore
    private ProjetInvestissement projetInvestissement;

}
