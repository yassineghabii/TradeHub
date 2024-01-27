package com.example.pifinance_back.Entities;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"titre"})
@ToString(exclude = {"titre", "transaction"})
public class OrdreAchat implements  Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idOrdreA;
    private double PrixAuMarcheA;

    @Column(name = "prix_limitea")
    private Double prixLimiteA;
    @JsonProperty("quantite")
    private int Quantite;
    private LocalDateTime Date_ordreA;
    @JsonProperty("type_ordre")
    @Enumerated(EnumType.STRING)
    private TypeOrdre typeOrdre;
    @Enumerated(EnumType.STRING)
    private StatutOrdre statut; // changed from 'Statut' to 'statut'
    @Enumerated(EnumType.STRING)
    private DureeValidite dureeValidite;

    @OneToOne (cascade = CascadeType.PERSIST)
    @JsonIgnore
    private Transaction transaction;
    @ManyToOne
    @JoinColumn(name = "idTitre", referencedColumnName = "idTitre")
    @JsonIgnore
    private Titre titre;

    private int refCarnet;
    // @Temporal(TemporalType.TIMESTAMP)
    // private LocalDateTime heureOrdreA;

    /* private int ordA;
     private int ordV;*/
    public void setPrixLimiteA(Double prixLimiteA) {
        this.prixLimiteA = prixLimiteA;
    }

    @ManyToOne
    @JoinColumn(name = "client_id") // Nom de la colonne faisant référence à l'ID du client
    private Client client;





}
