package com.example.blogengine.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "post_comments")
@NoArgsConstructor
public class PostComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NonNull
    private int id;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private PostComment parent;

    @ManyToOne
    @JoinColumn(name = "post_id")
    @NonNull
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @NonNull
    private User user;

    @NonNull
    private Date time;

    @NonNull
    private String text;
}
