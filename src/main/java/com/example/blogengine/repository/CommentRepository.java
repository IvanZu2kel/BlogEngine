package com.example.blogengine.repository;

import com.example.blogengine.model.PostComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<PostComment, Integer> {
    List<PostComment> findPostCommentsById(int id);
}
