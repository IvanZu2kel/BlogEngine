package com.example.blogengine.api.request;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ProfileRequest {
    private String name;
    private int removePhoto;
    private String email;
    private String password;
    private String photo;
}
