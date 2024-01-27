package com.example.pifinance_back.Services;

import com.example.pifinance_back.Entities.*;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;

public interface IWalletService {
    Wallet createWallet(Wallet wallet);

    List<Wallet> getWalletsByUser(Client user);
    public Portfolio createDefaultPortfolio(Client user)  ;
    public List<Wallet> getAllWallets() ;

    public Wallet getWalletDetailsById(Long id) ;

    void deleteWallet(Long id);

    void deleteCard(Long id);

    Wallet updateWallet(Wallet wallet);

    List<Wallet> getActiveWalletsForUser(Client user);

    Wallet activateWallet(Long walletId);

    Wallet deactivateWallet(Long walletId);

    BigDecimal getVirtualWalletBalance(Long walletId);

    List<Wallet> getWalletsByType(WalletEnum type);

    List<Wallet> getWalletsByCurrency(Currency currency);

    List<Wallet> getWalletsByUserAndType(Client user, WalletEnum type);

    BigDecimal convertCurrency(BigDecimal amount, String fromCurrencyCode, String toCurrencyCode);

    BigDecimal getRate(String fromCurrencyCode, String toCurrencyCode);

    void convertRealBalanceToCurrency(Long walletId, Currency targetCurrency);


    Card createCardForUser(Long userId, Card card);

    List<HistoriqueChargement> getHistoriqueByUserId(Long userId);

    List<HistoriqueChargement> getAllHistorique();

    Card getCardByIdWallet(Long walletId);

    List<Card> getAllCards();

    Card updateCardById(Long cardId, Card updatedCard);
}
