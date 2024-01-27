package com.example.pifinance_back.Repositories;
import com.example.pifinance_back.Entities.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface OrdreAchatRepository extends JpaRepository<OrdreAchat,Integer> {




    // Ajoutez une méthode pour récupérer les ordres d'achat en attente pour un titre
    @Query("SELECT o FROM OrdreAchat o WHERE o.titre = :titre  AND o.statut = 'EN_ATTENTE'")
    List<OrdreAchat> findOrdresAchatEnAttentePourTitre(@Param("titre") Titre titre);
    // Ajoutez une méthode pour récupérer les ordres d'achat en attente

    @Query("SELECT o FROM OrdreAchat o WHERE o.titre = :titre  AND o.statut = 'EN_ATTENTE'")
    List<OrdreAchat> findOrdresAchatEnAttente();



    List<OrdreAchat> findByTitreAndTypeOrdre(Titre titre, TypeOrdre typeOrdre);
    OrdreAchat findByTransaction(Transaction transaction);

    // List<OrdreAchat> findByTitreAndTypeOrdreAndStatutIn(Titre titre, TypeOrdre typeOrdre, Collection<StatutOrdre> statuts);
    @Query("SELECT o FROM OrdreAchat o WHERE o.titre = :titre AND o.typeOrdre = :typeOrdre AND o.statut IN :statuts")
    List<OrdreAchat> findByTitreAndTypeOrdreAndStatutIn(@Param("titre") Titre titre,
                                                        @Param("typeOrdre") TypeOrdre typeOrdre,
                                                        @Param("statuts") Collection<StatutOrdre> statuts);


    List<OrdreAchat> findAllByStatutIn(Collection<StatutOrdre> statuts);
    @Query("SELECT o FROM OrdreAchat o WHERE o.dureeValidite = :dureeValidite AND o.statut IN :statuts")
    List<OrdreAchat> findByDureeValiditeAndStatut(
            @Param("dureeValidite") DureeValidite dureeValidite,
            @Param("statuts") List<StatutOrdre> statuts
    );
    @Query("SELECT oa FROM OrdreAchat oa WHERE oa.titre.idTitre = :idTitre AND oa.statut IN :statuts AND oa.typeOrdre = :typeOrdre")
    List<OrdreAchat> findByTitreIdTitreAndStatutsAndTypeOrdre(@Param("idTitre") int idTitre, @Param("statuts") List<StatutOrdre> statuts, @Param("typeOrdre") TypeOrdre typeOrdre);
    List<OrdreAchat> findByTypeOrdre(TypeOrdre typeOrdre);
    List<OrdreAchat> findByTypeOrdreAndClientId(TypeOrdre typeOrdre, Long clientId);
    List<OrdreAchat> findByClientId ( Long clientId) ;
}
