package com.example.blogengine.repository;

import com.example.blogengine.model.PostVotes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostVotesRepository extends JpaRepository<PostVotes, Integer> {
    @Query("select pv from PostVotes pv where pv.post.id = :id")
    List<PostVotes> getAllByPostId(int id);
}
