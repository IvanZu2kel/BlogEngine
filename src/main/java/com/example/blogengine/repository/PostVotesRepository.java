package com.example.blogengine.repository;

import com.example.blogengine.model.PostVotes;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface PostVotesRepository extends JpaRepository<PostVotes, Integer> {
    @Query("select pv from PostVotes pv where pv.post.id = :id")
    List<PostVotes> getAllByPostId(int id);

    @Query("select count(pv) from PostVotes pv where pv.user.id = :id and pv.value = 1 group by pv.user.id")
    Optional<Integer> findLikesByUser(int id);

    @Query("select count(pv) from PostVotes pv where pv.user.id = :id and pv.value = 0 group by pv.user.id")
    Optional<Integer> findDislikesByUser(int id);

    @Query("select count(l) from PostVotes l where l.value = 1 ")
    Optional<Integer> findLikesCount();

    @Query("select count(l) from PostVotes l where l.value = 0 ")
    Optional<Integer> findDislikeCount();

    @Query("select pv from PostVotes pv where pv.post.id = :postId and pv.user.id = :id")
    PostVotes findVotes(int postId, int id);
}
