package com.example.pifinance_back.Repositories;

import com.example.pifinance_back.Entities.Client;
import com.example.pifinance_back.Entities.Wallet;
import com.example.pifinance_back.Entities.WalletEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;
import java.util.Optional;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {
    List<Wallet> findByType(@Param("type") WalletEnum type);
    List<Wallet> findByVirtualCurrency(@Param("VirtualCurrency") Currency currency);
    List<Wallet> findByUserAndType(@Param("user") Client user, @Param("type") WalletEnum type);
    List<Wallet> findByUserAndIsActive(@Param("user") Client user, @Param("isActive") boolean isActive);
    List<Wallet> findByUser(Client user);

    boolean existsByTokenTransaction(String tokenPaiement);
    Wallet findByUserId(Long userId);
    @Query("SELECT w.id_wallet FROM Wallet w WHERE w.user.id = :userId")
    Long findWalletIdByUserId(@Param("userId") Long userId);

    @Query("SELECT w ,w.user.firstname, w.user.lastname, w.user.cin, w.user.email FROM Wallet w")
    List<Object[]> findAllUserDetails();


    @Query("SELECT w FROM Wallet w WHERE w.id_wallet = :walletId")
    Wallet fetchWalletDetailsById(@Param("walletId") Long walletId);
    @Query("SELECT w.user.lastname, w.user.firstname, w.user.cin, w.user.email, w.id_wallet, w.createdAt, w.isActive, w.Real_balance, w.realCurrency, w.type, w.Virtual_balance, w.virtualCurrency FROM Wallet w JOIN w.user")
    List<Object[]> findAllUserNamesAndCardDetails();


    @Query("SELECT new com.example.pifinance_back.Entities.Wallet(w.id_wallet, w.Virtual_balance, w.Real_balance, w.realCurrency, w.type, w.virtualCurrency, w.createdAt, w.isActive, w.tokenTransaction) FROM Wallet w WHERE w.id_wallet = :id")
    Wallet findWalletDetailsById(@Param("id") Long id);
    @Transactional
    @Modifying
    @Query("UPDATE Wallet w SET w.Virtual_balance = :balance WHERE w.id_wallet = :id")
    int updateVirtualBalance(@Param("balance") BigDecimal balance, @Param("id") Long id);

}
