package com.example.pifinance_back.Services;

import com.example.pifinance_back.Entities.Topic;
import com.example.pifinance_back.Entities.TopicDTO;

import java.util.List;

public interface ITopicService {
    Topic addTopic (Topic topic, Long idUser);
    void deleteTopic(Long topicId);
    //List<Topic> getAllTopics();
    List<TopicDTO> getAllTopics();
    //Topic getTopic(Long topicId);
    TopicDTO getTopic(Long topicId);

    Long countPostsByIdTopic(Long idTopic);


}
