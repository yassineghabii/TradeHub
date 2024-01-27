package com.example.pifinance_back.Services;

import com.example.pifinance_back.Entities.VoteComment;

public interface VoteCommentService {
    VoteComment voteUserlike(Long IdComment, Long idUser);
    VoteComment voteUserdislike(Long IdComment, Long idUser);
    String getUserVoteStatus(Long commentId, Long userId);
}
