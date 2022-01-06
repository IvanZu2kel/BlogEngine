package com.example.blogengine.model;

import com.example.blogengine.model.enumerated.Role;
import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "users")
@NoArgsConstructor
@Accessors(chain = true)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NonNull
    @Column(columnDefinition = "int NOT NULL COMMENT 'id пользователя'")
    private int id;

    @Column(name = "is_moderator", columnDefinition = "tinyint NOT NULL COMMENT 'является ли пользователь модератором (может ли править\n" +
            "глобальные настройки сайта и модерировать посты)'")
    @NonNull
    private int isModerator;

    @Column(name = "reg_time", columnDefinition = "datetime NOT NULL COMMENT 'дата и время регистрации пользователя'")
    @NonNull
    private Date regTime;

    @Column(columnDefinition = "varchar(255) NOT NULL COMMENT 'имя пользователя'")
    @NonNull
    private String name;

    @Column(columnDefinition = "varchar(255) NOT NULL COMMENT 'e-mail пользователя'")
    @NonNull
    private String email;

    @Column(columnDefinition = "varchar(255) NOT NULL COMMENT 'хэш пароля пользователя'")
    @NonNull
    private String password;

    @Column(columnDefinition = "varchar(255) COMMENT 'код для восстановления пароля'")
    private String code;

    @Column(columnDefinition = "text COMMENT 'ссылка на фотографию'")
    private String photo;

    public Role getRole() {
        return isModerator == 1 ? Role.MODERATOR : Role.USER;
    }

}
