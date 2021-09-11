package com.example.blogengine.model;

import lombok.*;
import org.apache.coyote.http11.filters.SavedRequestInputFilter;

import javax.persistence.*;
import java.util.Date;

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

    @Column(name = "is_active")
    @NonNull
    private int isActive;

    @Column(name = "moderation_status")
    @Enumerated(value = EnumType.STRING)
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
    private String title;

    @NonNull
    private String text;

    @Column(name = "view_count")
    @NonNull
    private String viewCount;
}
