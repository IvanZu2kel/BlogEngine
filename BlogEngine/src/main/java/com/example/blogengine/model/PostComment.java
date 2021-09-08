package com.example.blogengine.model;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "post_comments")
public class PostComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    @NonNull
    private int id;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    @Getter
    @Setter
    private PostComment parent;

    @ManyToOne
    @JoinColumn(name = "post_id")
    @Getter
    @Setter
    @NonNull
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @Getter
    @Setter
    @NonNull
    private User user;

    @Getter
    @Setter
    @NonNull
    private Date time;

    @Getter
    @Setter
    @NonNull
    private String text;
}
