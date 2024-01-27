package com.example.pifinance_back.Controllers;


import com.example.pifinance_back.Entities.Comment;
import com.example.pifinance_back.Entities.CommentDTO;
import com.example.pifinance_back.Services.ICommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/Comment")
public class CommentController {

    @Autowired
    ICommentService commentService;

    @PostMapping("/addComment/{idUser}/{idPost}")
    @ResponseBody
    public Comment addComment(@RequestBody Comment comment,
                        @PathVariable("idUser") Long idUser,
                        @PathVariable("idPost") Long idPost){

        return commentService.addComment(comment, idUser, idPost);
    }

    @PutMapping("/updateComment/{commentId}")
    @ResponseBody
    public Comment updateComment(@PathVariable Long commentId,
                           @RequestBody Comment comment) {
        comment.setIdComment(commentId);
        return commentService.updateComment(comment);
    }

    @DeleteMapping("/deleteComment/{commentId}")
    @ResponseBody
    public void deleteComment(@PathVariable("commentId") Long commentId) {

        commentService.deleteComment(commentId);
    }

    @GetMapping("/getAllComments")
    @ResponseBody
    public List<CommentDTO> getAllComments() {
        List<CommentDTO> listComments = commentService.getAllComments();
        return listComments;
    }

    @GetMapping("/getComment/{commentId}")
    @ResponseBody
    public Comment getComment(@PathVariable("commentId") Long commentId) {
        return commentService.getComment(commentId);
    }



    @GetMapping("/byPost/{postId}")
    @ResponseBody
    public List<CommentDTO> getCommentsByPostId(@PathVariable Long postId) {
        List<CommentDTO> listComments = commentService.getCommentsByPostId(postId);
        return listComments;
    }

}
