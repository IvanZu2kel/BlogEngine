package com.example.blogengine.model;

import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "captcha_codes")
@NoArgsConstructor
@Accessors(chain = true)
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
    @NonNull
    @Column(name = "secret_code", columnDefinition = "tinytext")
    private String secretCode;

}
