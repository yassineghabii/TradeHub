package com.example.pifinance_back.Services;

import com.example.pifinance_back.Entities.TypeVote;
import com.example.pifinance_back.Entities.Vote;
import com.example.pifinance_back.Repositories.VoteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class VoteServiceImpl implements VoteService{
    private VoteRepository voteRepository;
    @Override
    public Vote voteUserlike(int idevent, int iduser) {
        Vote existingVote = voteRepository.findByIdEventAndIdUser(idevent,iduser);
        Vote vote=new Vote();
        if (existingVote == null){
            vote.setIdUser(iduser);
            vote.setIdEvent(idevent);
            vote.setTypeVote(TypeVote.LIKE);
            return voteRepository.save(vote);
        }else if (existingVote.getTypeVote()==TypeVote.DISLIKE) {
            existingVote.setTypeVote(TypeVote.LIKE);
            return voteRepository.save(existingVote);
        }
        return null;
    }

    @Override
    public Vote voteUserdislike(int idevent, int iduser) {
        Vote existingVote = voteRepository.findByIdEventAndIdUser(idevent,iduser);
        Vote vote=new Vote();
        if (existingVote == null){
            vote.setIdUser(iduser);
            vote.setIdEvent(idevent);
            vote.setTypeVote(TypeVote.DISLIKE);
            return voteRepository.save(vote);
        } else if (existingVote.getTypeVote()==TypeVote.LIKE) {
            existingVote.setTypeVote(TypeVote.DISLIKE);
            return voteRepository.save(existingVote);
        }
        return null;     }
}
