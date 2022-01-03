package com.example.blogengine.service;

import com.example.blogengine.exception.IncorrectFormatException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public interface StorageService {
    Object store(MultipartFile image) throws IOException, IncorrectFormatException;
}
