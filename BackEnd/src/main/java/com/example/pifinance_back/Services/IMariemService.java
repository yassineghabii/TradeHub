package com.example.pifinance_back.Services;

import com.example.pifinance_back.Entities.*;
import org.springframework.scheduling.annotation.Scheduled;

import javax.transaction.Transactional;
import java.util.List;

public interface IMariemService {

    public void matcherOrdres();

    List<TitreDTO> getTitres();

    List<OrdreAchat> getOrdresByType(TypeOrdre typeOrdre);

   List<OrdreVente> getOrdresVenteByType(TypeOrdre typeOrdre);

    public OrdreAchatLimiteDTO toOrdreAchatLimiteDTO(OrdreAchat ordre) ;
    public OrdreVenteLimiteDTO toOrdreVenteLimiteDTO(OrdreVente ordre) ;
    OrdreAchatAuMarcheDTO toOrdreAchatAuMarcheDTO(OrdreAchat ordre);

    OrdreVenteAuMarcheDTO toOrdreVenteAuMarcheDTO(OrdreVente ordre);

    public String afficherDonnees(String symbol, String interval);
    public void afficherToutesLesDonnees(List<String> symbols, String interval);
    List<String> obtenirSymbolesDynamiques();


    //public void passerOrdreAchat(int idTitre, TypeOrdre typeOrdre, int quantite);
    public void passerOrdreAchat(int idTitre, TypeOrdre typeOrdre, int quantite, Double prixLimiteA,DureeValidite dureeValidite, Long clientId);
    public void passerOrdreVente(int idTitre, TypeOrdre typeOrdre, int quantite, Double prixLimiteV,DureeValidite dureeValidite,Long clientId);

    @Transactional
    List<Transaction> getTransactionById(Long id);

 void annulerOrdreA(int idOrdre);

 void annulerOrdreV(int idOrdre);

 public CarnetOrdre genererCarnetOrdres(Titre titre);

    // public void simulerVariationPrixPourTousLesTitres();

    // Transaction exécuterOrdre(int idOrdre, double prixExecution);

    // void executerOrdresLimites();

    List<OrdreAchat> getAllOrdresA();

    public Titre ajouterTitre(String symbole, String nom, double prixOuverture, int quantite) ;

    void annulerOrdre(int idOrdre);

    List<Transaction> getAllTransactions();

    // List<Ordre> afficherCarnetOrdre(int idTitre);




    public List<LigneCarnet> genererCarnet(int idTitre) ;

    public String obtenirSymboleParIdTitre(int idTitre) ;


}
