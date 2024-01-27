package com.example.pifinance_back.Entities;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"titre"})
@ToString(exclude = {"titre", "transaction"})
public class OrdreVente implements  Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idOrdreV;
    private double PrixAuMarcheV;

    @Column(name = "prix_limitev")
    private Double prixLimiteV;
    private int Quantite;
    private LocalDateTime Date_ordreV;
    @Enumerated(EnumType.STRING)
    private TypeOrdre typeOrdre;
    @Enumerated(EnumType.STRING)
    private StatutOrdre statut; // changed from 'Statut' to 'statut'
    @Enumerated(EnumType.STRING)
    private DureeValidite dureeValidite ;


    @OneToOne (cascade = CascadeType.PERSIST)
    @JsonIgnore
    private Transaction transaction;
    @ManyToOne
    @JsonIgnore
    private Titre titre;
    private int refCarnet;
    // @Temporal(TemporalType.TIMESTAMP)
    // private LocalDateTime heureOrdreV;
    /*private int ordA;
    private int ordV;*/
    public void setPrixLimiteV(Double prixLimiteV) {
        this.prixLimiteV = prixLimiteV;
    }



    @ManyToOne
    @JoinColumn(name = "client_id") // Nom de la colonne faisant référence à l'ID du client
    private Client client;



}
