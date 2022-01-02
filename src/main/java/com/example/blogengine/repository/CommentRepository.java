package com.example.blogengine.repository;

import com.example.blogengine.model.PostComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<PostComment, Integer> {
    @Query("select c from PostComment c where c.post.id = :id ")
    List<PostComment> findPostCommentsById(int id);
}
