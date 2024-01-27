package com.example.pifinance_back.Services;

import com.example.pifinance_back.Entities.Comment;
import com.example.pifinance_back.Entities.TypeVote;
import com.example.pifinance_back.Entities.VoteComment;
import com.example.pifinance_back.Repositories.CommentRepository;
import com.example.pifinance_back.Repositories.VoteCommentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class VoteCommentServiceImpl implements VoteCommentService{

    private VoteCommentRepository voteRepository;
    private CommentRepository CommentRepository;
    @Override
    public VoteComment voteUserlike(Long IdComment, Long idUser) {

        Comment Comment = CommentRepository.findById(IdComment).orElse(null);

        VoteComment existingVote = voteRepository.findByIdCommentAndIdUser(IdComment,idUser);
        VoteComment vote=new VoteComment();

        if (existingVote == null){
            vote.setIdUser(idUser);
            vote.setIdComment(IdComment);
            vote.setTypeVote(TypeVote.LIKE);

            if (Comment != null) {
                Comment.setLikes(Comment.getLikes() + 1);
                CommentRepository.save(Comment);
            }

            return voteRepository.save(vote);
        }
        else if (existingVote.getTypeVote()==TypeVote.DISLIKE) {
            existingVote.setTypeVote(TypeVote.LIKE);

            if (Comment != null) {
                Comment.setLikes(Comment.getLikes() + 1);
                Comment.setDislikes(Comment.getDislikes() - 1);
                CommentRepository.save(Comment);
            }

            return voteRepository.save(existingVote);
        }
        else if (existingVote.getTypeVote()==TypeVote.LIKE) {
            existingVote.setTypeVote(TypeVote.NOVOTE);

            if (Comment != null) {
                Comment.setLikes(Comment.getLikes() - 1);
                CommentRepository.save(Comment);
            }

            return voteRepository.save(existingVote);
        }
        else if (existingVote.getTypeVote()==TypeVote.NOVOTE) {
            existingVote.setTypeVote(TypeVote.LIKE);

            if (Comment != null) {
                Comment.setLikes(Comment.getLikes() + 1);
                CommentRepository.save(Comment);
            }

            return voteRepository.save(existingVote);
        }

        return null;
    }

    @Override
    public VoteComment voteUserdislike(Long IdComment, Long idUser) {
        Comment Comment = CommentRepository.findById(IdComment).orElse(null);
        VoteComment existingVote = voteRepository.findByIdCommentAndIdUser(IdComment,idUser);
        VoteComment vote=new VoteComment();
        if (existingVote == null){
            vote.setIdUser(idUser);
            vote.setIdComment(IdComment);
            vote.setTypeVote(TypeVote.DISLIKE);
            if (Comment != null) {
                Comment.setDislikes(Comment.getDislikes() + 1);
                CommentRepository.save(Comment);
            }
            return voteRepository.save(vote);
        }
        else if (existingVote.getTypeVote()==TypeVote.LIKE) {
            existingVote.setTypeVote(TypeVote.DISLIKE);
            if (Comment != null) {
                Comment.setDislikes(Comment.getDislikes() + 1);
                Comment.setLikes(Comment.getLikes() - 1);
                CommentRepository.save(Comment);
            }
            return voteRepository.save(existingVote);
        }
        else if (existingVote.getTypeVote()==TypeVote.DISLIKE) {
            existingVote.setTypeVote(TypeVote.NOVOTE);
            if (Comment != null) {
                Comment.setDislikes(Comment.getDislikes() - 1);
                CommentRepository.save(Comment);
            }
            return voteRepository.save(existingVote);
        }
        else if (existingVote.getTypeVote()==TypeVote.NOVOTE) {
            existingVote.setTypeVote(TypeVote.DISLIKE);
            if (Comment != null) {
                Comment.setDislikes(Comment.getDislikes() + 1);
                CommentRepository.save(Comment);
            }
            return voteRepository.save(existingVote);
        }

        return null;
    }

    @Override
    public String getUserVoteStatus(Long CommentId, Long userId) {
        VoteComment existingVote = voteRepository.findByIdCommentAndIdUser(CommentId, userId);

        if (existingVote == null) {
            return "NOVOTE";
        } else {
            return existingVote.getTypeVote().toString();
        }
    }

}
