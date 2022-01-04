package com.example.blogengine.repository;

import com.example.blogengine.model.PostComment;
import com.example.blogengine.model.PostVotes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostCommentRepository extends JpaRepository<PostComment, Integer> {
    @Query("select pc from PostComment pc order by pc.id desc")
    List<PostComment> findLastPost();
}
