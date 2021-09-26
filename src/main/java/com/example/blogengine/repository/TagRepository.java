package com.example.blogengine.repository;

import com.example.blogengine.api.response.TagResponseAnswerQuery;
import com.example.blogengine.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tag, Integer> {

    @Query(value = "select t.name as name, count(t.id) as count from tags t join tag2post tp on tp.tag_id = t.id " +
            "join posts p on p.id = tp.post_id where p.is_active = 1 and p.moderation_status = 'ACCEPTED' and " +
            "p.`time` < NOW() group by tp.tag_id order by count desc", nativeQuery = true)
    List<TagResponseAnswerQuery> getRecentTags();
}
