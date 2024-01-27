package com.example.pifinance_back.Repositories;

import com.example.pifinance_back.Entities.OrdreVente;
import com.example.pifinance_back.Entities.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PortfolioRepository extends JpaRepository<Portfolio,Long> {
    @Query("SELECT p FROM Portfolio p JOIN FETCH p.client WHERE p.client.id = :clientId")
    Portfolio findByClientId(@Param("clientId") Long clientId);

}
