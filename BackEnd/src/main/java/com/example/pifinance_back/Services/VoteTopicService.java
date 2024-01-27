package com.example.pifinance_back.Services;

import com.example.pifinance_back.Entities.VoteTopic;

public interface VoteTopicService {
    VoteTopic voteUserlike(Long IdTopic, Long idUser);
    VoteTopic voteUserdislike(Long IdTopic, Long idUser);
    String getUserVoteStatus(Long topicId, Long userId);
}
