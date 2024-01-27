package com.example.pifinance_back.Entities;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CarnetOrdre implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  int idCarnetOrdre;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("prix DESC")
    private List<OrdreDetail> ordresAchat;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("prix ASC")
    private List<OrdreDetail> ordresVente;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "titre_id")
    private Titre titre;

    // Getters, and possibly methods for displaying or manipulating the orders...
}
