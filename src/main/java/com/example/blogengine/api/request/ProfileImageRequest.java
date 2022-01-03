package com.example.blogengine.api.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ProfileImageRequest {
    private String name;
    private int removePhoto;
    private String email;
    private String password;
    private MultipartFile photo;
}
