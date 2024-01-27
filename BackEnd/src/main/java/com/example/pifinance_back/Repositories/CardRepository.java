package com.example.pifinance_back.Repositories;

import com.example.pifinance_back.Entities.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository

public interface CardRepository extends JpaRepository<Card, Long> {
    @Query("SELECT c FROM Card c WHERE c.wallet.id_wallet = :walletId")
    Optional<Card> findByWallet_Id(@Param("walletId") Long walletId);
        @Query("SELECT c FROM Card c WHERE c.wallet.id_wallet = :walletId")
        List<Card> findAllByWalletId(@Param("walletId") Long walletId);
    @Query("SELECT c.id_card FROM Card c WHERE c.wallet.id_wallet = :walletId")
    Long findCardIdByWalletId(@Param("walletId") Long walletId);
    @Query("SELECT c.wallet.user.lastname, c.wallet.user.firstname, c.number, c.exp_month, c.exp_year, c.cvc, c.CardType, c.ville, c.codePostal, c.pays, c.adresseDeFacturation, c.currency, c.prenomNom ,c.wallet.id_wallet,c.id_card FROM Card c JOIN c.wallet w JOIN w.user")
    List<Object[]> findAllUserNamesAndCardDetails();

}




