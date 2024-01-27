package com.example.pifinance_back.Services;

import com.example.pifinance_back.Entities.VotePost;

public interface VotePostService {
    VotePost voteUserlike(Long IdComment, Long idUser);
    VotePost voteUserdislike(Long IdComment, Long idUser);
    String getUserVoteStatus(Long commentId, Long userId);
}
