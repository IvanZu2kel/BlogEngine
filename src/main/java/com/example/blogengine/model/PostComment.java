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

    @ManyToOne(optional=false, cascade=CascadeType.ALL)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(optional=false, cascade=CascadeType.ALL)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NonNull
    private Date time;

    @Column(columnDefinition = "text")
    @NonNull
    private String text;
}
