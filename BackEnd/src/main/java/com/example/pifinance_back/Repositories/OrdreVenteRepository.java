package com.example.pifinance_back.Repositories;
import com.example.pifinance_back.Entities.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface OrdreVenteRepository extends JpaRepository<OrdreVente,Integer> {

    //jdid

    // Ajoutez une méthode pour récupérer les ordres de vente en attente pour un titre
    @Query("SELECT o FROM OrdreVente o WHERE o.titre = :titre  AND o.statut = 'EN_ATTENTE'")
    List<OrdreVente> findOrdresVenteEnAttentePourTitre(@Param("titre") Titre titre);


    // Ajoutez une méthode pour récupérer les ordres d'achat en attente

    // Ajoutez une méthode pour récupérer les ordres de vente en attente


    @Query("SELECT o FROM OrdreVente o WHERE o.titre = :titre AND o.statut = 'EN_ATTENTE'")
    List<OrdreVente> findOrdresVenteEnAttente();

    List<OrdreVente> findByTitreAndTypeOrdre(Titre titre, TypeOrdre typeOrdre);

    OrdreVente findByTransaction(Transaction transaction);

    //List<OrdreVente> findByTitreAndTypeOrdreAndStatutIn(Titre titre, TypeOrdre typeOrdre, Collection<StatutOrdre> statuts);

    @Query("SELECT v FROM OrdreVente v WHERE v.titre = :titre AND v.typeOrdre = :typeOrdre AND v.statut IN :statuts")
    List<OrdreVente> findByTitreAndTypeOrdreAndStatutIn(@Param("titre") Titre titre,
                                                        @Param("typeOrdre") TypeOrdre typeOrdre,
                                                        @Param("statuts") Collection<StatutOrdre> statuts);
    List<OrdreVente> findAllByStatutIn(Collection<StatutOrdre> statuts);
    @Query("SELECT o FROM OrdreVente o WHERE o.dureeValidite = :dureeValidite AND o.statut IN :statuts")
    List<OrdreVente> findByDureeValiditeAndStatut(
            @Param("dureeValidite") DureeValidite dureeValidite,
            @Param("statuts") List<StatutOrdre> statuts
    );
    @Query("SELECT ov FROM OrdreVente ov WHERE ov.titre.idTitre = :idTitre AND ov.statut IN :statuts AND ov.typeOrdre = :typeOrdre")
    List<OrdreVente> findByTitreIdTitreAndStatutsAndTypeOrdre(@Param("idTitre") int idTitre, @Param("statuts") List<StatutOrdre> statuts, @Param("typeOrdre") TypeOrdre typeOrdre);
    List<OrdreVente> findByTypeOrdre(TypeOrdre typeOrdre);
    List<OrdreVente> findByTypeOrdreAndClientId(TypeOrdre typeOrdre, Long clientId);
    List<OrdreVente> findByClientId ( Long clientId) ;

}

