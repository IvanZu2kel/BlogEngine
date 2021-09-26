package com.example.blogengine.api.response;

import com.example.blogengine.model.User;
import lombok.Data;
import lombok.NonNull;

@Data
public class UserPostResponse {
    int id;
    String name;

    public UserPostResponse(User user) {
        this.id = user.getId();
        this.name = user.getName();
    }
}
