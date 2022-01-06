package com.example.blogengine.repository;

import com.example.blogengine.model.Tag2Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface Tag2PostRepository extends JpaRepository<Tag2Post, Integer> {
    @Query("select t2p from Tag2Post t2p where t2p.post_id = :id")
    List<Tag2Post> findAllByPostId(int id);

    @Query("select t2p from Tag2Post t2p where t2p.post_id = :post_id and t2p.tag_id = :tag_id")
    Optional<Tag2Post> findTagByPost(int tag_id, int post_id);

    @Query("select t2p from Tag2Post t2p where t2p.post_id = :id")
    List<Tag2Post> findTagsByPost(int id);
}
