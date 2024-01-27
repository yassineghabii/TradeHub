package com.example.pifinance_back.Repositories;

import com.example.pifinance_back.Entities.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {
}
