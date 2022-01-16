package com.example.blogengine.controllers;

import com.example.blogengine.AbstractTest;
import com.example.blogengine.api.request.ModeratorRequest;
import com.example.blogengine.model.Post;
import com.example.blogengine.model.User;
import com.example.blogengine.model.enumerated.ModerationStatus;
import com.example.blogengine.repository.PostRepository;
import com.example.blogengine.repository.UserRepository;
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

import java.util.Date;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(value = {"classpath:application-test.properties"})
public class ApiGeneralControllerTest extends AbstractTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    public void setup() {
        super.setup();
    }

    @Test
    void getInit() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/init")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("DevPub"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("ivan_zukkel@mail.ru"));
    }

    @Test
    @WithMockUser(username = "test3@test.ru", authorities = "user:write")
    void postModerateNoModerator() throws Exception {
        ModeratorRequest moderatorRequest = new ModeratorRequest()
                .setPostId(2)
                .setDecision("accept");
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/moderation")
                        .principal(() -> "test3@test.ru")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(moderatorRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    void getTags() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/tag")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("query", "новости"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
