package com.example.pifinance_back.Repositories;

import com.example.pifinance_back.Entities.Topic;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TopicRepository extends CrudRepository<Topic, Long>  {

    @Query("SELECT COUNT(p) FROM Post p WHERE p.topic.idTopic = :idTopic")
    Long countPostsByIdTopic(@Param("idTopic") Long idTopic);

    @Query("SELECT w.idTopic FROM Topic w WHERE w.user.id = :userId")
    Long findTopicIdByUserId(@Param("userId") Long userId);

}
