package com.example.blogengine.service.implementation;

import com.example.blogengine.api.response.tags.TagResponse;
import com.example.blogengine.api.response.tags.TagResponseAnswerQuery;
import com.example.blogengine.api.response.tags.TagsResponse;
import com.example.blogengine.repository.TagRepository;
import com.example.blogengine.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;

    @Override
    public TagsResponse getTags(String query) {
        List<TagResponseAnswerQuery> listTags = tagRepository.getRecentTags();
        double normParam = (double) listTags.get(0).getCount()/listTags.size();
        List<TagResponse> tagResponses = new ArrayList<>();
        for (TagResponseAnswerQuery tag : listTags) {
            TagResponse tagResponse = new TagResponse(tag.getName(), ((double) tag.getCount()/listTags.size()/normParam));
            tagResponses.add(tagResponse);
        }
        TagsResponse tagsResponse = new TagsResponse();
        tagsResponse.setTags(tagResponses);

        return tagsResponse;
    }
}
