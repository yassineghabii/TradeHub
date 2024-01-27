package com.example.pifinance_back.Services;

import com.example.pifinance_back.Entities.*;
import com.example.pifinance_back.Repositories.ClientRepository;
import com.example.pifinance_back.Repositories.CommentRepository;
import com.example.pifinance_back.Repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService implements ICommentService{

    @Autowired
    CommentRepository commentRepo;
    @Autowired
    PostRepository postRepo;
    @Autowired
    ClientRepository userRepo;


    @Override
    public Comment addComment(Comment comment, Long idUser, Long idPost) {
        Client user = userRepo.findById(idUser).orElse(null);
        Post post = postRepo.findById(idPost).orElse(null);
        comment.setUser(user);
        comment.setPost(post);

        comment.setCreationDate(new Date());
        comment.setLikes(0);
        comment.setDislikes(0);
        comment.setModified(false);
        return commentRepo.save(comment);
    }

    @Override
    public Comment updateComment(Comment comment) {
        if (commentRepo.existsById(comment.getIdComment())) {
            Comment commentExist = commentRepo.findById(comment.getIdComment()).orElse(null);
            if (commentExist != null) {
                commentExist.setContent(comment.getContent());
                commentExist.setCreationDate(new Date());
                commentExist.setModified(true);

                return commentRepo.save(commentExist);
            }
        }
        return null;
    }

    @Override
    public void deleteComment(Long commentId) {
        commentRepo.deleteById(commentId);

    }

    /*@Override
    public List<Comment> getAllComments() {
        return (List<Comment>) commentRepo.findAll();
    }*/

    @Override
    public List<CommentDTO> getAllComments() {
        Iterable<Comment> commentsIterable = commentRepo.findAll();
        List<Comment> comments = new ArrayList<>();
        commentsIterable.forEach(comments::add);
        return comments.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CommentDTO> getCommentsByPostId(Long idPost) {
        List<Comment> comments = commentRepo.findByPostId(idPost);
        return comments.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private CommentDTO convertToDto(Comment comment) {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setIdComment(comment.getIdComment());
        commentDTO.setContent(comment.getContent());
        commentDTO.setLikes(comment.getLikes());
        commentDTO.setDislikes(comment.getDislikes());
        commentDTO.setCreationDate(comment.getCreationDate());
        commentDTO.setModified(comment.getModified());
        // Ajoutez l'ID de l'utilisateur
        if (comment.getUser() != null) {
            commentDTO.setUserId(comment.getUser().getId());
        }
        return commentDTO;
    }

    @Override
    public Comment getComment(Long commentId) {
        return commentRepo.findById(commentId).orElse(null);
    }



    /*@Override
    public List<Comment> getCommentsByPostId(Long postId) {
        return (List<Comment>) commentRepo.findByPostId(postId);
    }*/
}
