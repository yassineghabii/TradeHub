package com.example.pifinance_back.Repositories;

import com.example.pifinance_back.Entities.FinancialProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FinancialProfileRepository extends JpaRepository<FinancialProfile, Long> {
    Optional<FinancialProfile> findByClientId(Long clientId);
}
