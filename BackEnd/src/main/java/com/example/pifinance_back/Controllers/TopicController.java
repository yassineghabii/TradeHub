package com.example.pifinance_back.Controllers;

import com.example.pifinance_back.Entities.Topic;
import com.example.pifinance_back.Entities.TopicDTO;
import com.example.pifinance_back.Services.ITopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/Topic")
public class TopicController {

    @Autowired
    ITopicService topicService;

    @PostMapping("/addTopic/{idUser}")
    @ResponseBody
    public Topic addTopic(@RequestBody Topic topic,
                          @PathVariable("idUser") Long idUser){

        return topicService.addTopic(topic, idUser);
    }

    @DeleteMapping("/deleteTopic/{topicId}")
    @ResponseBody
    public void deleteTopic(@PathVariable("topicId") Long topicId) {

        topicService.deleteTopic(topicId);
    }

    @GetMapping("/getAllTopics")
    @ResponseBody
    public List<TopicDTO> getAllTopics() {
        List<TopicDTO> listTopics = topicService.getAllTopics();
        return listTopics;
    }

    @GetMapping("/getTopic/{topicId}")
    @ResponseBody
    public TopicDTO getTopic(@PathVariable("topicId") Long topicId) {
        return topicService.getTopic(topicId);
    }


    @GetMapping("/postCount/{idTopic}")
    public Long countPostsByIdTopic(@PathVariable Long idTopic) {
        return topicService.countPostsByIdTopic(idTopic);
    }

}
