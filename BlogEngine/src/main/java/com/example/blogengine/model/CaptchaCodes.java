package com.example.blogengine.model;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "captcha_codes")
public class CaptchaCodes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    @NonNull
    private int id;

    @Getter
    @Setter
    @NonNull
    private Date time;

    @Getter
    @Setter
    @NonNull
    private String code;

    @Column(name = "secret_code")
    @Getter
    @Setter
    @NonNull
    private String secretCode;
}
