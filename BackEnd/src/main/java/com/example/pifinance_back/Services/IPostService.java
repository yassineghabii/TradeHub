package com.example.pifinance_back.Services;

import com.example.pifinance_back.Entities.Post;
import com.example.pifinance_back.Entities.PostDTO;

import java.util.List;

public interface IPostService {
    Post addPost (Post post, Long idUser, Long idTopic);
    Post updatePost (Post post);
    void deletePost(Long postId);
    //List<Post> getAllPosts();
    List<PostDTO> getAllPosts();

    Post getPost(Long postId);

    //List<Post> getPostsByTopicId(Long topicId);
    List<PostDTO> getPostsByTopicId(Long topicId);
    Long countCommentsByIdPost(Long idPost);
}
