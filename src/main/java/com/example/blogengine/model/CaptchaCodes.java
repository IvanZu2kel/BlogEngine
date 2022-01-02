package com.example.blogengine.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
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
    private LocalDateTime time;
    @NonNull
    @Column(columnDefinition = "tinytext")
    private String code;

    @Column(name = "secret_code", columnDefinition = "tinytext")
    @NonNull
    private String secretCode;

}
