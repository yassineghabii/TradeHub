package com.example.pifinance_back.Controllers;

import com.example.pifinance_back.Entities.VoteComment;
import com.example.pifinance_back.Services.VoteCommentService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/VoteComment")
@CrossOrigin(origins = "http://localhost:4200")
public class VoteCommentRestController {

    private VoteCommentService voteService;
    @PostMapping("/VoteLike/{idComment}/{idUser}")
    public VoteComment voteUserlike(@PathVariable("idComment") Long idComment,
                                  @PathVariable("idUser") Long idUser){
        return voteService.voteUserlike(idComment,idUser);
    }
    @PostMapping("/VoteDislike/{idComment}/{idUser}")
    public VoteComment voteUserDislike(@PathVariable("idComment") Long idComment,
                                     @PathVariable("idUser") Long idUser){
        return voteService.voteUserdislike(idComment,idUser);
    }

    @GetMapping("/status/{CommentId}/{userId}")
    public ResponseEntity<String> getUserVoteStatus(
            @PathVariable Long CommentId,
            @PathVariable Long userId) {
        String status = voteService.getUserVoteStatus(CommentId, userId);
        return ResponseEntity.ok(status);
    }

}
