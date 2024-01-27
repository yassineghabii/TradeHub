package com.example.pifinance_back.Repositories;

import com.example.pifinance_back.Entities.VoteComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteCommentRepository extends JpaRepository<VoteComment,Integer> {
    VoteComment findByIdCommentAndIdUser(Long IdComment, Long idUser);
}
