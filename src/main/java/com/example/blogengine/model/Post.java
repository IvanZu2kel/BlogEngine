package com.example.blogengine.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "posts")
@AllArgsConstructor
@NoArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NonNull
    private int id;

    @Column(name = "is_active", columnDefinition = "tinyint(1)")
    @NonNull
    private int isActive;

    @Column(name = "moderation_status", columnDefinition = "enum('NEW','ACCEPTED','DECLINED') default 'NEW'")
    @Enumerated(value = EnumType.STRING)
    @NonNull
    private ModerationStatus moderationStatus;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "moderator_id")
    private User moderator;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NonNull
    private Date time;

    @NonNull
    @Column(columnDefinition = "varchar(255)")
    private String title;

    @NonNull
    @Column(columnDefinition = "text")
    private String text;

    @Column(name = "view_count", columnDefinition = "int")
    @NonNull
    private int viewCount;

    @OneToMany(mappedBy="post")
    private List<Tag2Post> tags;

    @OneToMany(mappedBy="post")
    private List<PostVotes> like;

    @OneToMany(mappedBy="post")
    private List<PostComment> comments;

}
