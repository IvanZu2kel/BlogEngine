package com.example.blogengine.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "captcha_codes")
@NoArgsConstructor
public class CaptchaCodes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NonNull
    private int id;

    @NonNull
    private Date time;

    @NonNull
    @Column(columnDefinition = "tinytext")
    private String code;

    @Column(name = "secret_code", columnDefinition = "tinytext")
    @NonNull
    private String secretCode;

}
