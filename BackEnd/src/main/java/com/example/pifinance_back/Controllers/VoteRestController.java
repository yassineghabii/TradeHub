package com.example.pifinance_back.Controllers;

import com.example.pifinance_back.Entities.Vote;
import com.example.pifinance_back.Services.VoteService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/Vote")
@CrossOrigin(origins = "http://localhost:4200")
public class VoteRestController {
    private VoteService voteService;
    @PostMapping("/VoteLike/{idevent}/{iduser}")
    public Vote voteUserlike(@PathVariable("idevent") int idevent,@PathVariable("iduser") int iduser){
        return voteService.voteUserlike(idevent,iduser);
    }
    @PostMapping("/VoteDislike/{idevent}/{iduser}")
    public Vote voteUserDislike(@PathVariable("idevent") int idevent,@PathVariable("iduser") int iduser){
        return voteService.voteUserdislike(idevent,iduser);
    }


}
