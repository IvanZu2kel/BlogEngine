package com.example.blogengine.controllers;

import com.example.blogengine.AbstractTest;
import com.example.blogengine.api.request.PostRequest;
import com.example.blogengine.model.Post;
import com.example.blogengine.model.Tag2Post;
import com.example.blogengine.model.User;
import com.example.blogengine.model.enumerated.ModerationStatus;
import com.example.blogengine.repository.PostRepository;
import com.example.blogengine.repository.TagRepository;
import com.example.blogengine.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(value = {"classpath:application-test.properties"})
public class ApiPostControllerTest extends AbstractTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TagRepository tagRepository;

    private Pageable pageable;
    private Page<Post> def;
    private Page<Post> popular;
    private Page<Post> best;
    private Page<Post> early;

    @BeforeEach
    public void setup() {
        super.setup();
        pageable = PageRequest.of(0 / 10, 10);
        def = postRepository.findAllPostsByTimeDesc(pageable);
        popular = postRepository.findAllPostsByCommentsDesc(pageable);
        best = postRepository.findAllPostsByVotesDesc(pageable);
        early = postRepository.findAllPostsByTime(pageable);
    }

    @Test
    void getPosts() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/post")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.count").value(def.getTotalElements())).andReturn();

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("status", "popular"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.count").value(popular.getTotalElements())).andReturn();

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("status", "best"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.count").value(best.getTotalElements())).andReturn();

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("status", "early"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.count").value(early.getTotalElements()));
    }

    @Test
    @WithMockUser(username = "test@test.ru", authorities = "user:write")
    void editPost() throws Exception {
        List<String> tags = new ArrayList<>();
        tags.add("Образ");
        tags.add("Понимание");
        PostRequest postRequest = new PostRequest()
                .setText("Таким образом, понимание сути ресурсосберегающих " +
                        "технологий выявляет срочную потребность кластеризации " +
                        "усилий. Принимая во внимание показатели успешности, " +
                        "высокотехнологичная концепция общественного уклада " +
                        "предоставляет широкие возможности для новых предложений.")
                .setActive(1)
                .setTitle("Давайте разбираться: герцог графства коронован!")
                .setTags(tags)
                .setTimestamp(new Date().getTime());

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(postRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result").value(true)).andReturn();

        postRequest.setTitle("12");
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(postRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors.title").value("Заголовок не установлен"));
    }

    @Test
    void searchPosts() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("query", "   "))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.count").value(def.getTotalElements())).andReturn();

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("query", "кре"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void searchPostsByDate() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/post/byDate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("date", "2021-11-01"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.count").value(1)).andReturn();

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/post/byDate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("date", "2021-11-11"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.count").value(0));
    }

    @Test
    void searchPostsByTag() throws Exception {
        Pageable pageable = PageRequest.of(0,20);
        Page<Post> pagePost = postRepository.findAllPostsByTag("политэс", pageable);
        long totalElements = pagePost.getTotalElements();
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/post/byTag")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("tag", "политэс"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.count").value(totalElements)).andReturn();

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/post/byTag")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("tag", "помощ"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.count").value(0));
    }

    @Test
    void searchPostsById() throws Exception {
        List<Post> posts = postRepository.findPosts();
        Post post = posts.get(0);
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/post/{id}", post.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(post.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(post.getTitle())).andReturn();

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/post/{id}", 3124)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @WithMockUser(username = "test@test.ru", authorities = "user:write")
    void putPostsById() throws Exception {
        List<String> tags = new ArrayList<>();
        tags.add("Образ");
        tags.add("Понимание");
        PostRequest postRequest = new PostRequest()
                .setText("Таким образом, понимание сути ресурсосберегающих " +
                        "технологий выявляет срочную потребность кластеризации " +
                        "усилий. Принимая во внимание показатели успешности, " +
                        "высокотехнологичная концепция общественного уклада " +
                        "предоставляет широкие возможности для новых предложений.")
                .setActive(1)
                .setTitle("Давайте разбираться: герцог графства коронован!")
                .setTags(tags)
                .setTimestamp(new Date().getTime());

        List<Post> posts = postRepository.findPosts();
        Post post = posts.get(0);

        Optional<User> user = userRepository.findByEmail("test@test.ru");
        post.setUser(user.get());
        Post save = postRepository.save(post);
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/post/{id}", save.getId())
                        .principal(() -> "test@test.ru")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(postRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result").value(true)).andReturn();

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/post/{id}", 3124)
                        .principal(() -> "test@test.ru")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(postRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @WithMockUser(username = "test@test.ru", authorities = "user:write")
    void getMyPosts() throws Exception {
        Page<Post> inactive = postRepository.findPostsMyInactive(pageable, "test@test.ru");
        Page<Post> pending = postRepository.findPostsMyIsActive("NEW", "test@test.ru", pageable);
        Page<Post> declined = postRepository.findPostsMyIsActive("DECLINED", "test@test.ru", pageable);
        Page<Post> published = postRepository.findPostsMyIsActive("ACCEPTED", "test@test.ru", pageable);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/post/my")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("status", "inactive"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.count").value(inactive.getTotalElements())).andReturn();

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/post/my")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("status", "pending"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.count").value(pending.getTotalElements())).andReturn();

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/post/my")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("status", "declined"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.count").value(declined.getTotalElements())).andReturn();

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/post/my")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("status", "published"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.count").value(published.getTotalElements()));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/post/my")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("status", "publish"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(username = "test3@test.ru", authorities = "user:moderate")
    void getPostsModerate() throws Exception {
        User moder = userRepository.findByEmail("test3@test.ru").orElseThrow();
        Page<Post> newPosts = postRepository.findPostsByModerate(ModerationStatus.NEW, pageable);
        Page<Post> accepted = postRepository.findPostsMyModerate(ModerationStatus.ACCEPTED, moder.getId(), pageable);
        Page<Post> declined = postRepository.findPostsMyModerate(ModerationStatus.DECLINED, moder.getId(), pageable);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/post/moderation")
                        .principal(() -> "test3@test.ru")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("status", "new"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.count").value(newPosts.getTotalElements())).andReturn();

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/post/moderation")
                        .principal(() -> "test3@test.ru")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("status", "declined"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.count").value(declined.getTotalElements())).andReturn();

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/post/moderation")
                        .principal(() -> "test3@test.ru")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("status", "accepted"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.count").value(accepted.getTotalElements()));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/post/moderation")
                        .principal(() -> "test3@test.ru")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("status", "publish"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @WithMockUser(username = "test3@test.ru", authorities = "user:write")
    void getPostsModerateNoModer() throws Exception {
        User moder = userRepository.findByEmail("test3@test.ru").orElseThrow();

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/post/moderation")
                        .principal(() -> "test3@test.ru")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("status", "new"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden()).andReturn();

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/post/moderation")
                        .principal(() -> "test3@test.ru")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("status", "declined"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden()).andReturn();

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/post/moderation")
                        .principal(() -> "test3@test.ru")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("status", "accepted"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }


}
