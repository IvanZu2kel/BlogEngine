package com.example.blogengine.service.implementation;

import com.example.blogengine.api.response.tags.Tag4TagsResponse;
import com.example.blogengine.api.response.tags.TagsResponse;
import com.example.blogengine.model.Post;
import com.example.blogengine.model.Tag;
import com.example.blogengine.model.enumerated.ModerationStatus;
import com.example.blogengine.repository.PostRepository;
import com.example.blogengine.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {
    private final PostRepository postRepository;

    @Override
    public TagsResponse getTags(String query) {
        Map<Tag, Double> tagMap = new HashMap<>();
        List<Tag4TagsResponse> tag4TagsResponseList = new ArrayList<>();
        TagsResponse tagsResponse = new TagsResponse();
        List<Post> postList = new ArrayList<>();
        postRepository.findPosts().forEach(p -> {
            if (p.getIsActive() == 1 && p.getModerationStatus().equals(ModerationStatus.ACCEPTED) && p.getTime().compareTo(new Date()) < 1)
                postList.add(p);
            p.getTags().forEach(tag -> tagMap.compute(tag, (k, v) -> (v == null) ? v = 1.0 : v + 1.0));
        });
        double maxWeight = tagMap.values().stream().max(Double::compare).orElse(0.0) / (double) postList.size();
        if (maxWeight != 0.0) {
            double k = 1.0 / maxWeight;
            tagMap.forEach((tag, value) -> {
                if (tag.getName().contains(query)) {
                    Tag4TagsResponse tag4TagsResponse = new Tag4TagsResponse();
                    tag4TagsResponse.setName(tag.getName());
                    tag4TagsResponse.setWeight(Math.max(value / postList.size() * k, 0.3));
                    tag4TagsResponseList.add(tag4TagsResponse);
                }
            });
            Tag4TagsResponse[] tags4PostResponse = tag4TagsResponseList.toArray(new Tag4TagsResponse[0]);
            tagsResponse.setTags(tags4PostResponse);
        }
        return tagsResponse;
    }
}
