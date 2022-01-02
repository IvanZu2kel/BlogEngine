package com.example.blogengine.repository;

import com.example.blogengine.model.PostVotes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostVotesRepository extends JpaRepository<PostVotes, Integer> {
    @Query("select pv from PostVotes pv where pv.post.id = :id")
    List<PostVotes> getAllByPostId(int id);

    @Query("select count(pv) from PostVotes pv where pv.user.id = :id and pv.value = 1 group by pv.user.id")
    Optional<Integer> findLikesByUser(int id);

    @Query("select count(pv) from PostVotes pv where pv.user.id = :id and pv.value = 0 group by pv.user.id")
    Optional<Integer> findDislikesByUser(int id);
}
