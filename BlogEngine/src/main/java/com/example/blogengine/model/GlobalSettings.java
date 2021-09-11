package com.example.blogengine.model;

import lombok.*;

import javax.persistence.*;

@Data
@Entity
@Table(name = "global_settings")
@NoArgsConstructor
public class GlobalSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NonNull
    private int id;

    @NonNull
    private String code;

    @NonNull
    private String name;

    @NonNull
    private String value;
}
