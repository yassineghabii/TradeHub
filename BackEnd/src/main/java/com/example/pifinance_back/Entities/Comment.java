package com.example.pifinance_back.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Comment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idComment;
    private String content;
    private Integer likes;
    private Integer dislikes;
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    private Boolean modified;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "user_id")
    private Client user;
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "post_id")
    private Post post;

}
