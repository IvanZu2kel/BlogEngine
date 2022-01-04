package com.example.blogengine.model;

import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "post_votes")
@NoArgsConstructor
@Accessors(chain = true)
public class PostVotes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NonNull
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable=false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable=false)
    private Post post;

    @NonNull
    private Date time;

    @Column(name = "value", columnDefinition = "tinyint(1)")
    @NonNull
    private byte value;
}
