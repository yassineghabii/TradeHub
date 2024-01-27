package com.example.pifinance_back.Controllers;

import com.example.pifinance_back.Entities.VotePost;
import com.example.pifinance_back.Services.VotePostService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/VotePost")
@CrossOrigin(origins = "http://localhost:4200")
public class VotePostRestController {

    private VotePostService voteService;
    @PostMapping("/VoteLike/{idPost}/{idUser}")
    public VotePost voteUserlike(@PathVariable("idPost") Long idPost,
                                  @PathVariable("idUser") Long idUser){
        return voteService.voteUserlike(idPost,idUser);
    }
    @PostMapping("/VoteDislike/{idPost}/{idUser}")
    public VotePost voteUserDislike(@PathVariable("idPost") Long idPost,
                                     @PathVariable("idUser") Long idUser){
        return voteService.voteUserdislike(idPost,idUser);
    }

    @GetMapping("/status/{PostId}/{userId}")
    public ResponseEntity<String> getUserVoteStatus(
            @PathVariable Long PostId,
            @PathVariable Long userId) {
        String status = voteService.getUserVoteStatus(PostId, userId);
        return ResponseEntity.ok(status);
    }

}
