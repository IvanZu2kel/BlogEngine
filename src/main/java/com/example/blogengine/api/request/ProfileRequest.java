package com.example.blogengine.api.request;

import lombok.Data;

@Data
public class ProfileRequest {
    private String name;
    private int removePhoto;
    private String email;
    private String password;
    private String photo;
}
