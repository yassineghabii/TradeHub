package com.example.pifinance_back.Services;

import com.example.pifinance_back.Entities.Post;
import com.example.pifinance_back.Entities.TypeVote;
import com.example.pifinance_back.Entities.VotePost;
import com.example.pifinance_back.Repositories.PostRepository;
import com.example.pifinance_back.Repositories.VotePostRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class VotePostServiceImpl implements VotePostService{

    private VotePostRepository voteRepository;
    private PostRepository PostRepository;
    @Override
    public VotePost voteUserlike(Long IdPost, Long idUser) {

        Post Post = PostRepository.findById(IdPost).orElse(null);

        VotePost existingVote = voteRepository.findByIdPostAndIdUser(IdPost,idUser);
        VotePost vote=new VotePost();

        if (existingVote == null){
            vote.setIdUser(idUser);
            vote.setIdPost(IdPost);
            vote.setTypeVote(TypeVote.LIKE);

            if (Post != null) {
                Post.setLikes(Post.getLikes() + 1);
                PostRepository.save(Post);
            }

            return voteRepository.save(vote);
        }
        else if (existingVote.getTypeVote()==TypeVote.DISLIKE) {
            existingVote.setTypeVote(TypeVote.LIKE);

            if (Post != null) {
                Post.setLikes(Post.getLikes() + 1);
                Post.setDislikes(Post.getDislikes() - 1);
                PostRepository.save(Post);
            }

            return voteRepository.save(existingVote);
        }
        else if (existingVote.getTypeVote()==TypeVote.LIKE) {
            existingVote.setTypeVote(TypeVote.NOVOTE);

            if (Post != null) {
                Post.setLikes(Post.getLikes() - 1);
                PostRepository.save(Post);
            }

            return voteRepository.save(existingVote);
        }
        else if (existingVote.getTypeVote()==TypeVote.NOVOTE) {
            existingVote.setTypeVote(TypeVote.LIKE);

            if (Post != null) {
                Post.setLikes(Post.getLikes() + 1);
                PostRepository.save(Post);
            }

            return voteRepository.save(existingVote);
        }

        return null;
    }

    @Override
    public VotePost voteUserdislike(Long IdPost, Long idUser) {
        Post Post = PostRepository.findById(IdPost).orElse(null);
        VotePost existingVote = voteRepository.findByIdPostAndIdUser(IdPost,idUser);
        VotePost vote=new VotePost();
        if (existingVote == null){
            vote.setIdUser(idUser);
            vote.setIdPost(IdPost);
            vote.setTypeVote(TypeVote.DISLIKE);
            if (Post != null) {
                Post.setDislikes(Post.getDislikes() + 1);
                PostRepository.save(Post);
            }
            return voteRepository.save(vote);
        }
        else if (existingVote.getTypeVote()==TypeVote.LIKE) {
            existingVote.setTypeVote(TypeVote.DISLIKE);
            if (Post != null) {
                Post.setDislikes(Post.getDislikes() + 1);
                Post.setLikes(Post.getLikes() - 1);
                PostRepository.save(Post);
            }
            return voteRepository.save(existingVote);
        }
        else if (existingVote.getTypeVote()==TypeVote.DISLIKE) {
            existingVote.setTypeVote(TypeVote.NOVOTE);
            if (Post != null) {
                Post.setDislikes(Post.getDislikes() - 1);
                PostRepository.save(Post);
            }
            return voteRepository.save(existingVote);
        }
        else if (existingVote.getTypeVote()==TypeVote.NOVOTE) {
            existingVote.setTypeVote(TypeVote.DISLIKE);
            if (Post != null) {
                Post.setDislikes(Post.getDislikes() + 1);
                PostRepository.save(Post);
            }
            return voteRepository.save(existingVote);
        }

        return null;
    }

    @Override
    public String getUserVoteStatus(Long PostId, Long userId) {
        VotePost existingVote = voteRepository.findByIdPostAndIdUser(PostId, userId);

        if (existingVote == null) {
            return "NOVOTE";
        } else {
            return existingVote.getTypeVote().toString();
        }
    }

}
