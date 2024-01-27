package com.example.pifinance_back.Repositories;

import com.example.pifinance_back.Entities.Titre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TitreRepository extends JpaRepository<Titre,Integer> {

    @Query("SELECT t FROM Titre t WHERE t.PrixPlusHaut = :prixPlusHaut")
    List<Titre> findByPrixPlusHaut(double prixPlusHaut);

    @Query("SELECT t FROM Titre t WHERE t.PrixPlusBas = :prixPlusBas")
    List<Titre> findByPrixPlusBas(double prixPlusBas);
    @Query("SELECT t.Symbole FROM Titre t WHERE t.idTitre = :idTitre")
    String findSymbolTitreById(int idTitre);

}
