package com.example.pifinance_back.Services;

import com.example.pifinance_back.Entities.Topic;
import com.example.pifinance_back.Entities.TypeVote;
import com.example.pifinance_back.Entities.VoteTopic;
import com.example.pifinance_back.Repositories.TopicRepository;
import com.example.pifinance_back.Repositories.VoteTopicRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class VoteTopicServiceImpl implements VoteTopicService{

    private VoteTopicRepository voteRepository;
    private TopicRepository topicRepository;
    @Override
    public VoteTopic voteUserlike(Long IdTopic, Long idUser) {

        Topic topic = topicRepository.findById(IdTopic).orElse(null);

        VoteTopic existingVote = voteRepository.findByIdTopicAndIdUser(IdTopic,idUser);
        VoteTopic vote=new VoteTopic();

        if (existingVote == null){
            vote.setIdUser(idUser);
            vote.setIdTopic(IdTopic);
            vote.setTypeVote(TypeVote.LIKE);

            if (topic != null) {
                topic.setLikes(topic.getLikes() + 1);
                topicRepository.save(topic);
            }

            return voteRepository.save(vote);
        }
        else if (existingVote.getTypeVote()==TypeVote.DISLIKE) {
            existingVote.setTypeVote(TypeVote.LIKE);

            if (topic != null) {
                topic.setLikes(topic.getLikes() + 1);
                topic.setDislikes(topic.getDislikes() - 1);
                topicRepository.save(topic);
            }

            return voteRepository.save(existingVote);
        }
        else if (existingVote.getTypeVote()==TypeVote.LIKE) {
            existingVote.setTypeVote(TypeVote.NOVOTE);

            if (topic != null) {
                topic.setLikes(topic.getLikes() - 1);
                topicRepository.save(topic);
            }

            return voteRepository.save(existingVote);
        }
        else if (existingVote.getTypeVote()==TypeVote.NOVOTE) {
            existingVote.setTypeVote(TypeVote.LIKE);

            if (topic != null) {
                topic.setLikes(topic.getLikes() + 1);
                topicRepository.save(topic);
            }

            return voteRepository.save(existingVote);
        }

        return null;
    }

    @Override
    public VoteTopic voteUserdislike(Long IdTopic, Long idUser) {
        Topic topic = topicRepository.findById(IdTopic).orElse(null);
        VoteTopic existingVote = voteRepository.findByIdTopicAndIdUser(IdTopic,idUser);
        VoteTopic vote=new VoteTopic();
        if (existingVote == null){
            vote.setIdUser(idUser);
            vote.setIdTopic(IdTopic);
            vote.setTypeVote(TypeVote.DISLIKE);
            if (topic != null) {
                topic.setDislikes(topic.getDislikes() + 1);
                topicRepository.save(topic);
            }
            return voteRepository.save(vote);
        }
        else if (existingVote.getTypeVote()==TypeVote.LIKE) {
            existingVote.setTypeVote(TypeVote.DISLIKE);
            if (topic != null) {
                topic.setDislikes(topic.getDislikes() + 1);
                topic.setLikes(topic.getLikes() - 1);
                topicRepository.save(topic);
            }
            return voteRepository.save(existingVote);
        }
        else if (existingVote.getTypeVote()==TypeVote.DISLIKE) {
            existingVote.setTypeVote(TypeVote.NOVOTE);
            if (topic != null) {
                topic.setDislikes(topic.getDislikes() - 1);
                topicRepository.save(topic);
            }
            return voteRepository.save(existingVote);
        }
        else if (existingVote.getTypeVote()==TypeVote.NOVOTE) {
            existingVote.setTypeVote(TypeVote.DISLIKE);
            if (topic != null) {
                topic.setDislikes(topic.getDislikes() + 1);
                topicRepository.save(topic);
            }
            return voteRepository.save(existingVote);
        }

        return null;
    }

    @Override
    public String getUserVoteStatus(Long topicId, Long userId) {
        VoteTopic existingVote = voteRepository.findByIdTopicAndIdUser(topicId, userId);

        if (existingVote == null) {
            return "NOVOTE";
        } else {
            return existingVote.getTypeVote().toString();
        }
    }

}
