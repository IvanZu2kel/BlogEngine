package com.example.blogengine.controllers;

import com.example.blogengine.AbstractTest;
import com.example.blogengine.api.request.ProfileRequest;
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

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(value = {"classpath:application-test.properties"})
public class ApiProfileControllerTest extends AbstractTest {
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        super.setup();
    }

    @Test
    @WithMockUser(username = "test@test.ru", authorities = "user:write")
    void postProfileWithoutImage() throws Exception {
        ProfileRequest profileRequest = new ProfileRequest()
                .setName("Ivan")
                .setPassword("qwerty123");
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/profile/my")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(profileRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result").value(true)).andReturn();
    }

    @Test
    @WithMockUser(username = "test@test.ru", authorities = "user:write")
    void postProfileWithoutImageError() throws Exception {
        ProfileRequest profileRequest = new ProfileRequest()
                .setName("Ivan")
                .setPassword("q12");
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/profile/my")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(profileRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result").value(false)).andReturn();
    }
}
