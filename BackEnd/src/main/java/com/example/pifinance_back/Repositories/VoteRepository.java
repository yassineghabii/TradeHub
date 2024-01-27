package com.example.pifinance_back.Repositories;

import com.example.pifinance_back.Entities.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteRepository extends JpaRepository<Vote,Integer> {
    Vote findByIdEventAndIdUser(int idEvent, int idUser);

}
