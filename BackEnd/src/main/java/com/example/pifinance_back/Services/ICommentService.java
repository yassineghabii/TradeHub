package com.example.pifinance_back.Services;

import com.example.pifinance_back.Entities.Comment;
import com.example.pifinance_back.Entities.CommentDTO;

import java.util.List;

public interface ICommentService {

    Comment addComment (Comment comment, Long idUser, Long idPost);
    Comment updateComment (Comment comment);
    void deleteComment(Long commentId);
    //List<Comment> getAllComments();
    List<CommentDTO> getAllComments();

    Comment getComment(Long commentId);

    //List<Comment> getCommentsByPostId(Long postId);
    List<CommentDTO> getCommentsByPostId(Long postId);

}
