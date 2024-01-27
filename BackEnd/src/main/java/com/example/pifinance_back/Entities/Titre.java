    package com.example.pifinance_back.Entities;

    import com.fasterxml.jackson.annotation.JsonIgnore;
    import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
    import lombok.*;

    import javax.persistence.*;
    import java.io.Serializable;
    import java.time.LocalDate;
    import java.time.LocalDateTime;
    import java.util.List;
    import java.util.Set;

    @Entity
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    @JsonIgnoreProperties({"ordresAchat", "ordresVente"})

    public class Titre implements Serializable {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int idTitre ;
        private String Symbole ;
        private String  Nom ;
        private LocalDateTime Date_Creation;
        private double PrixOuverture ;
        private double PrixPlusHaut ;
        private double PrixPlusBas ;
        private double PrixActuel ;
        private Double PrixCloture;
       // private double VolumeEchange ;
        private int Quantite ;
        private LocalDateTime DateMaj;
        @OneToMany(mappedBy = "titre", cascade = CascadeType.ALL)
        @JsonIgnore
        private Set<OrdreAchat> ordresAchat;

        @OneToMany(mappedBy = "titre", fetch = FetchType.LAZY)
        @JsonIgnore
        private List<OrdreVente> ordresVente;

       /* @PrePersist
        @PreUpdate
        public void prePersist() {
            Date_ordre = LocalDate.from(LocalDateTime.now());*/
        }

