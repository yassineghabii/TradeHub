package com.example.pifinance_back.Repositories;

import com.example.pifinance_back.Entities.VoteTopic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteTopicRepository extends JpaRepository<VoteTopic,Integer> {
    VoteTopic findByIdTopicAndIdUser(Long IdTopic, Long idUser);
}
