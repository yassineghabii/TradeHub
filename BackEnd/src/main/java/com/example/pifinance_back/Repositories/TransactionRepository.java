package com.example.pifinance_back.Repositories;

import com.example.pifinance_back.Entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction,Integer> {
    @Query("SELECT t FROM Transaction t WHERE t.idTransaction IN " +
            "(SELECT o.transaction.idTransaction FROM OrdreAchat o WHERE o.client.id = :idClient) OR " +
            "t.idTransaction IN " +
            "(SELECT v.transaction.idTransaction FROM OrdreVente v WHERE v.client.id = :idClient)")
    List<Transaction> findTransactionsByClientId(@Param("idClient") Long idClient);

}
