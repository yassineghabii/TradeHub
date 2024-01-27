package com.example.pifinance_back.Controllers;

import com.example.pifinance_back.Entities.VoteTopic;
import com.example.pifinance_back.Services.VoteTopicService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/VoteTopic")
@CrossOrigin(origins = "http://localhost:4200")
public class VoteTopicRestController {

    private VoteTopicService voteService;
    @PostMapping("/VoteLike/{idTopic}/{idUser}")
    public VoteTopic voteUserlike(@PathVariable("idTopic") Long idTopic,
                                  @PathVariable("idUser") Long idUser){
        return voteService.voteUserlike(idTopic,idUser);
    }
    @PostMapping("/VoteDislike/{idTopic}/{idUser}")
    public VoteTopic voteUserDislike(@PathVariable("idTopic") Long idTopic,
                                     @PathVariable("idUser") Long idUser){
        return voteService.voteUserdislike(idTopic,idUser);
    }

    @GetMapping("/status/{topicId}/{userId}")
    public ResponseEntity<String> getUserVoteStatus(
            @PathVariable Long topicId,
            @PathVariable Long userId) {
        String status = voteService.getUserVoteStatus(topicId, userId);
        return ResponseEntity.ok(status);
    }

}
