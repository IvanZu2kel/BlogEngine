package com.example.blogengine.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Data
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
