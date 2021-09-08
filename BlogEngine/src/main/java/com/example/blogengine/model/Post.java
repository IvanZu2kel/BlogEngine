package com.example.blogengine.model;

import lombok.*;
import org.apache.coyote.http11.filters.SavedRequestInputFilter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "posts")
@AllArgsConstructor
@NoArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    @NonNull
    private int id;

    @Column(name = "is_active")
    @Getter
    @Setter
    @NonNull
    private int isActive;

    @Column(name = "moderation_status")
    @Getter
    @Setter
    @Enumerated(value = EnumType.STRING)
    private ModerationStatus moderationStatus;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "moderator_id")
    @Getter
    @Setter
    private User moderator;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @Getter
    @Setter
    private User user;

    @Getter
    @Setter
    @NonNull
    private Date time;

    @Getter
    @Setter
    @NonNull
    private String title;

    @Getter
    @Setter
    @NonNull
    private String text;

    @Column(name = "view_count")
    @Getter
    @Setter
    @NonNull
    private String viewCount;
}
