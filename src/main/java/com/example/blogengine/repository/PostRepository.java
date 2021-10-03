package com.example.blogengine.repository;

import com.example.blogengine.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {

    @Query(value = "SELECT * from posts p where p.moderation_status = 'NEW'", nativeQuery = true)
    List<Post> findPostByModerationStatus();

    @Query(value = "select * from posts p where p.is_active = 1 and p.moderation_status = 'ACCEPTED' and p.`time` < NOW() ORDER BY " +
            "(select count(*) from post_comments c where c.post_id = p.id) DESC", nativeQuery = true)
    Page<Post> findAllPostsByCommentsDesc(Pageable pageable);

    @Query(value = "select * from posts p where p.is_active = 1 and p.moderation_status = 'ACCEPTED' and p.`time` < NOW() ORDER BY " +
            "(select sum(value) from post_votes c where c.post_id = p.id) desc", nativeQuery = true)
    Page<Post> findAllPostsByVotesDesc(Pageable pageable);

    @Query(value = "select * from posts p where p.is_active = 1 and p.moderation_status = 'ACCEPTED' and p.`time` < NOW() ORDER BY " +
            "p.time", nativeQuery = true)
    Page<Post> findAllPostsByTime(Pageable pageable);

    @Query(value = "select *  from posts p where is_active = 1 and moderation_status = 'ACCEPTED' and p.`time` < NOW() ORDER BY " +
            "time desc", nativeQuery = true)
    Page<Post> findAllPostsByTimeDesc(Pageable pageable);

    @Query(value = "select p.* from posts p where p.is_active = 1 and p.moderation_status = 'ACCEPTED' and p.`time` < NOW() ORDER BY " +
            "p.time", nativeQuery = true)
    List<Post> findAllPosts();

    @Query(value = "select * from posts p where p.`time` like %:query% and p.is_active = 1 and p.moderation_status = 'ACCEPTED' and p.`time` < NOW() " +
            "order by p.`time` desc", nativeQuery = true)
    Page<Post> findAllPostsBySearch(String query, Pageable pageable);

    @Query(value = "SELECT * FROM posts p WHERE p.`time` LIKE :date% AND p.is_active = 1 AND p.moderation_status = 'ACCEPTED' AND p.`time` < NOW() " +
            "ORDER BY p.time", nativeQuery = true)
    Page<Post> findAllPostsByDate(@Param("date") String date, Pageable pageable);

    @Query(value = "SELECT * FROM posts p JOIN tag2post tp ON tp.post_id = p.id JOIN tags t ON t.id = tp.tag_id WHERE t.name = :tag AND p.is_active = 1 " +
            "AND p.moderation_status = 'ACCEPTED' AND p.`time` < NOW() ORDER BY p.time", nativeQuery = true)
    Page<Post> findAllPostsByTag(@Param("tag") String tag, Pageable pageable);

    @Query(value = "SELECT * FROM posts p WHERE p.id = :id", nativeQuery = true)
    Post findPostById(@Param("id") int id);

    @Query(value = "SELECT * FROM posts p WHERE p.id = :id AND p.moderation_status = 'ACCEPTED' AND p.`time` < NOW() " +
            "ORDER BY p.time", nativeQuery = true)
    Post findPostAcceptedById(int id);
}