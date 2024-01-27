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
public class Formation implements Serializable {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Integer id;
private String nom;
private String thematique;
private String description;
private LocalDate date_debut;
private LocalDate date_fin;
private int capacite;
private String organisateur;

@ManyToMany(mappedBy="formations",cascade = CascadeType.ALL)
@JsonIgnore
private Set<Client> clients;
}
