package com.example.blogengine.model;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "users")
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NonNull
    private int id;

    @NonNull
    @Column(name = "is_moderator")
    private int isModerator;

    @NonNull
    @Column(name = "reg_time")
    private Date regTime;

    @NonNull
    private String name;

    @NonNull
    private String email;

    @NonNull
    private String password;

    private String code;

    private String photo;

}
