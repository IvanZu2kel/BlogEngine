package com.example.blogengine.model;

import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "global_settings")
@NoArgsConstructor
@Accessors(chain = true)
public class GlobalSettings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NonNull
    private int id;

    @NonNull
    @Column(columnDefinition = "varchar(255)")
    private String code;

    @NonNull
    @Column(columnDefinition = "varchar(255)")
    private String name;

    @NonNull
    @Column(columnDefinition = "varchar(255)")
    private String value;
}
