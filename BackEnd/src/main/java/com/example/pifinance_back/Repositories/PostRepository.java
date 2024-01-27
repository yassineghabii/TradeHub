package com.example.pifinance_back.Repositories;

import com.example.pifinance_back.Entities.Post;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends CrudRepository<Post, Long> {

    @Query("SELECT c FROM Post c WHERE c.topic.idTopic= :idTopic")
    List<Post> findByTopicId(@Param("idTopic") Long idTopic);

    @Query("SELECT COUNT(p) FROM Comment p WHERE p.post.idPost = :idPost")
    Long countCommentsByIdPost(@Param("idPost") Long idPost);

}
