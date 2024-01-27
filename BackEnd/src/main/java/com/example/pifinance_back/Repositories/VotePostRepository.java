package com.example.pifinance_back.Repositories;

import com.example.pifinance_back.Entities.VotePost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VotePostRepository extends JpaRepository<VotePost,Integer> {
    VotePost findByIdPostAndIdUser(Long IdPost, Long idUser);
}
