package com.example.blogengine.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Data
@Entity
@Table(name = "tag2post")
@NoArgsConstructor
@Accessors(chain = true)
public class Tag2Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int post_id;

    private int tag_id;
}
