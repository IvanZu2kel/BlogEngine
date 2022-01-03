package com.example.blogengine.api.response.posts;

import com.example.blogengine.model.Post;
import com.example.blogengine.model.User;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserPostResponse {
    int id;
    String name;
}
