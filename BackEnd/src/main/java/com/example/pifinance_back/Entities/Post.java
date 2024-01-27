package com.example.pifinance_back.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Post implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPost;
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
    @JoinColumn(name = "topic_id")
    private Topic topic;
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Comment> comments;
}
