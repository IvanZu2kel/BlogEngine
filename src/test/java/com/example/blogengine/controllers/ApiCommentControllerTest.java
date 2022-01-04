package com.example.blogengine.controllers;

import com.example.blogengine.AbstractTest;
import com.example.blogengine.api.request.CommentRequest;
import com.example.blogengine.model.Post;
import com.example.blogengine.repository.PostCommentRepository;
import com.example.blogengine.repository.PostRepository;
import com.example.blogengine.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(value = {"classpath:application-test.properties"})
public class ApiCommentControllerTest extends AbstractTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private PostCommentRepository postCommentRepository;

    @BeforeEach
    public void setup() {
        super.setup();
    }

    @Test
    @WithMockUser(username = "test@test.ru", authorities = "user:write")
    void editComment() throws Exception {
        List<Post> posts = postRepository.findPosts();
        Post post = posts.get(0);

        CommentRequest commentRequest = new CommentRequest()
                .setPostId(post.getId())
                .setText("new text");
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/comment")
                        .principal(() -> "test@test.ru")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(commentRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id")
                        .value(postCommentRepository.findLastPost().get(0).getId())).andReturn();

        commentRequest.setPostId(1212);
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/comment")
                        .principal(() -> "test@test.ru")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(commentRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
